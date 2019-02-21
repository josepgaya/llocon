import { Component, Input, Output, OnInit, ViewChild, ElementRef, EventEmitter, HostListener } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { FormControl } from "@angular/forms";

import { GridOptions, ColDef, ColGroupDef } from 'ag-grid-community/main';
import { AgGridNg2 } from 'ag-grid-angular/main';
import { Resource } from 'angular4-hal';
import { Observable, Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators'
import * as moment from "moment";

import { DatagridActionsRendererComponent } from "./datagrid-actions-renderer.component";
import { BooleanEditorComponent } from "./boolean-editor.component";
import { NumericEditorComponent } from "./numeric-editor.component";
import { DatagridHeaderComponent } from "./datagrid-header.component";
import { DatagridMessageService, PaginacioInfo } from "./datagrid-message.service";
import { RestapiService } from "../../core/restapi/restapi-service";
import {
    RestapiResource,
    RestapiField
} from "../../core";

export interface DatagridConfig {
    resourceName: string;
    columns?: DatagridColumn[];
    headerTitle?: string;
    pare?: any;
    height?: any;
    lovMode?: boolean;
    readOnly?: boolean;
    editable?: boolean;
    staticData?: boolean;
    dataAttribute?: string;
    showTopBar?: boolean;
    detailConfig?: DatagridConfig;
}
export interface DatagridColumn {
    field?: string;
    headerName?: string;
    editable?: boolean;
    width?: number;
    sortable?: boolean;
}
export interface DatagridEventParams {
    resourceName: string,
    rowId?: string | number;
    rowData?: any;
    detailResourceName?: string,
    pare?: any,
    parePk?: any
}

@Component( {
    selector: 'datagrid',
    template: `
<datagrid-top-bar
    *ngIf="!config.lovMode && config.showTopBar"
    [headerTitle]="config.headerTitle"
    [details]="config.detailConfig"
    [readOnly]="config.readOnly"
    [editable]="config.editable"
    [staticData]="config.staticData"
    (buttonRefreshClick)="refresh()"
    (buttonCreateClick)="onNewElementClicked()"
    (buttonCreateChildClick)="onSelectedRowAddChildClicked()"
    (buttonEditClick)="onSelectedRowEditClicked()"
    (buttonDeleteClick)="onSelectedRowDeleteClicked()"></datagrid-top-bar>
<ag-grid-angular
    #dataGrid
    [ngClass]="theme"
    [style.height]="styleHeight"
    [style.margin-bottom]="styleMarginBottom"
    [gridOptions]="gridOptions"
    (gridReady)="onGridReady($event)"
    (modelUpdated)="onModelUpdated($event)"
    (rowEditingStarted)="onRowEditingStarted($event)"
    (rowValueChanged)="onRowValueChanged($event)"
    (rowSelected)="onRowSelected($event)"
    (rowClicked)="onRowClicked($event)"
    (rowDoubleClicked)="onRowDoubleClicked($event)"
    (bodyScroll)="onBodyScroll($event)"
    (cellMouseOver)="onCellMouseOver($event)"
    (cellMouseOut)="onCellMouseOut($event)"></ag-grid-angular>`,
    styles: [`
ag-grid-angular {
}
`],
    providers: [
        RestapiService,
        DatagridMessageService]
} )
export class DatagridComponent implements OnInit {

    @Input() config: DatagridConfig;
    @Input()
    set data( data: any[] ) {
        if ( this.gridOptions.api ) {
            this.gridOptions.api.setRowData( data );
            this.actualitzarNumElements( data.length );
        } else {
            this.staticData = data;
        }
    }

    @Output() actionCreate: EventEmitter<any> = new EventEmitter();
    @Output() actionCreateChild: EventEmitter<any> = new EventEmitter();
    @Output() actionEdit: EventEmitter<any> = new EventEmitter();
    @Output() actionDelete: EventEmitter<any> = new EventEmitter();
    @Output() rowClick: EventEmitter<any> = new EventEmitter();
    @Output() rowDoubleClick: EventEmitter<any> = new EventEmitter();
    @Output() selectionChanged: EventEmitter<any> = new EventEmitter();

    @ViewChild( 'dataGrid' ) dataGridElement: AgGridNg2;
    //@ViewChild( 'datagridRowActions' ) datagridRowActionsElement: DatagridRowActionsComponent;

    private theme = "ag-theme-material";
    private styleHeight;
    private marginBottom = 0;
    private styleMarginBottom = this.marginBottom + "px";
    private rowActionsActive = false;
    private staticData;
    private formInfo;
    private editingRowData;
    private hoverRowData;
    private hoverRowDomNode;
    private gridOptions: GridOptions = {
        suppressCellSelection: true,
        suppressRowClickSelection: false,
        suppressContextMenu: true,
        stopEditingWhenGridLosesFocus: true,
        context: {
            rootComponent: this/*,
            resourceName: this.config.resourceName*/
        },
        overlayLoadingTemplate: '<span class="ag-overlay-loading-center" style="padding-top: 2em"><i class="fa fa-cog fa-spin fa-2x fa-fw"></i></span>'
    };
    private numElements: any = '#';
    private firstRow: number = 0;
    private lastRow: number = 0;
    private filtre;
    private filtreSubscription: Subscription;
    private formResourceId: any;

    ngOnInit() {
        this.refresh();
        if ( this.config.detailConfig ) {
            this.detailConfigureParams(
                this.gridOptions,
                this.config.detailConfig );
        }
        this.rowActionsActive = !this.config.lovMode && !this.config.readOnly;
        this.gridOptions.suppressRowClickSelection = this.config.readOnly;
        //this.gridOptions.rowSelection = ( this.config.lovMode ) ? 'single' : 'multiple';
        this.gridOptions.rowSelection = 'single';
        if ( this.config.editable && !this.config.readOnly ) {
            this.gridOptions.editType = 'fullRow';
            this.gridOptions.singleClickEdit = true;
        }
        if ( this.config.height === 'auto' || this.config.detailConfig ) {
        } else {
            if ( this.config.height ) {
                this.styleHeight = this.config.height + 'px';
            } else {
                if ( this.config.lovMode ) {
                    this.styleHeight = '500px';
                } else {
                    let fixedHeight = 128;
                    fixedHeight += this.marginBottom;
                    this.styleHeight = this.sanitizer.bypassSecurityTrustStyle(
                        'calc(100vh - ' + fixedHeight + 'px)' );
                    //this.styleHeight = "100%";
                }
            }
        }
    }

    public refresh() {
        if ( this.config.staticData ) {
            let colDefs = this.generarColumnDefs(
                this.config.columns,
                this.config.headerTitle,
                this.config.resourceName,
                this.config.detailConfig );
            if ( this.gridOptions.api ) {
                this.gridOptions.api.setColumnDefs( colDefs );
                this.gridOptions.api.setRowData( this.staticData );
            } else {
                this.gridOptions.rowData = this.staticData;
                this.gridOptions.columnDefs = colDefs;
            }
            this.actualitzarNumElements( this.staticData.length );
        } else {
            this.resourceService.configure( this.config.resourceName ).subscribe(( resourceConfig: RestapiResource ) => {
                this.formInfo = resourceConfig;
                let colDefs = this.generarColumnDefs(
                    this.config.columns,
                    this.config.headerTitle,
                    this.config.resourceName,
                    this.config.detailConfig,
                    resourceConfig );
                if ( this.gridOptions.api ) {
                    this.gridOptions.api.setColumnDefs( colDefs );
                } else {
                    this.gridOptions.columnDefs = colDefs;
                }
            } );
            this.gridOptions.rowModelType = "infinite";
            if ( this.gridOptions.api ) {
                this.gridOptions.api.setDatasource( this.generarDataSource() );
            } else {
                this.gridOptions.datasource = this.generarDataSource();
            }
        }
    }

    onGridReady( params ) {
        params.api.hidePopupMenu();
        if ( this.config.height === 'auto' || this.config.detailConfig ) {
            params.api.setGridAutoHeight( true );
        }
    }

    onModelUpdated( params ) {
        params.api.sizeColumnsToFit();
    }

    onRowEditingStarted( params ) {
        this.gridOptions.api.deselectAll();
        this.editingRowData = JSON.parse( JSON.stringify( params.data ) );
    }
    onRowValueChanged( params ) {
        let hasToUpdate = false;
        let dataForUpdate = params.data;
        let hasToRefresh = false;
        let dataForRefresh = params.data;
        let thisFormInfo = this.formInfo;
        let thisEditingRowData = this.editingRowData;
        let columnDefs = this.gridOptions.columnDefs;
        if ( columnDefs.length == 1 && ( <ColGroupDef>columnDefs[0] ).children ) {
            columnDefs = ( <ColGroupDef>columnDefs[0] ).children;
        }
        columnDefs.forEach( function( column ) {
            let field = ( <ColDef>column ).field;
            let fieldInfo;
            thisFormInfo.fields.forEach( function( f ) {
                if ( f.name === field ) {
                    fieldInfo = f;
                }
            } );
            let valuePrev = thisEditingRowData[field];
            let valueCurr = params.data[field];
            if ( fieldInfo.type === 'STRING' ) {
                if ( valueCurr === '' ) {
                    valueCurr = undefined;
                    dataForRefresh[field] = valueCurr;
                    hasToRefresh = true;
                }
            }
            if ( valuePrev !== valueCurr ) {
                dataForUpdate[field] = valueCurr;
                hasToUpdate = true;
            }
        } );
        if ( hasToRefresh ) {
            params.node.setData( dataForRefresh );
        }
        if ( hasToUpdate ) {
            this.resourceService.update( dataForUpdate ).subscribe(( resource: Resource ) => {
                params.node.setData( resource );
                this.gridOptions.api.flashCells( { rowNodes: [params.node] } )
            } );
        }
    }

    onBodyScroll( params ) {
        this.actualitzarNumElements(
            this.numElements,
            this.gridOptions.api );
    }
    onCellMouseOver( params ) {
        if ( this.rowActionsActive ) {
            this.hoverRowData = params.node.data;
            let rowDomNode = params.event.target;
            while ( rowDomNode.getAttribute( 'role' ) !== 'row' ) {
                rowDomNode = rowDomNode.parentNode;
            }
            this.hoverRowDomNode = rowDomNode;
            var actionsComponent = rowDomNode.lastElementChild.getElementsByTagName('datagrid-actions-renderer')[0];
            this.messageService.sendHoverRow(
                    params.node );
        }
    }
    onCellMouseOut( params ) {
        if ( this.rowActionsActive ) {
            this.hoverRowData = null;
            /*let isCursorInBotons = this.datagridRowActionsElement.isCursorInside( params.event );
            if ( !isCursorInBotons ) {
                this.hoverRowData = null;
                this.hoverRowDomNode = null;
            }*/
            this.hoverRowDomNode = null;
            this.messageService.clearHoverRow();
        }
    }

    onRowSelected() {
        this.selectionChanged.emit( this.gridOptions.api.getSelectedRows() );
        this.messageService.sendSelection(
            this.gridOptions.api.getSelectedRows() );
        /*if ( !this.config.lovMode && this.rowHoverActionsActive ) {
            this.datagridRowActionsElement.updateBotonsBackground();
        }*/
    }
    onNewElementClicked( resourceName, api ) {
        if ( !this.config.readOnly ) {
            let outputParams: DatagridEventParams = {
                resourceName: resourceName ? resourceName : this.config.resourceName
            }
            outputParams.pare = this.config.pare;
            this.actionCreate.emit( outputParams );
        }
    }
    onSelectedRowAddChildClicked( resourceName, detailResourceName, api ) {
        if ( !this.config.readOnly ) {
            let currentApi = api ? api : this.gridOptions.api
            let selectedRow = currentApi.getSelectedRows()[0];
            let outputParams: DatagridEventParams = {
                rowId: selectedRow.id,
                rowData: selectedRow,
                resourceName: resourceName ? resourceName : this.config.resourceName,
                detailResourceName: detailResourceName,
                pare: this.config.pare
            }
            this.actionCreateChild.emit( outputParams );
        }
    }
    onSelectedRowEditClicked( resourceName, api ) {
        if ( !this.config.readOnly ) {
            let currentApi = api ? api : this.gridOptions.api
            let selectedRow = currentApi.getSelectedRows()[0];
            let outputParams: DatagridEventParams = {
                rowId: selectedRow.id,
                rowData: selectedRow,
                resourceName: resourceName ? resourceName : this.config.resourceName,
                pare: this.config.pare
            }
            this.actionEdit.emit( outputParams );
        }
    }
    onSelectedRowsDeleteClicked( resourceName, api ) {
        if ( !this.config.readOnly ) {
            let currentApi = api ? api : this.gridOptions.api
            let selectedRow = currentApi.getSelectedRows()[0];
            let outputParams: DatagridEventParams = {
                rowId: selectedRow.id,
                rowData: selectedRow,
                resourceName: resourceName ? resourceName : this.config.resourceName,
                pare: this.config.pare
            }
            this.actionDelete.emit( outputParams );
        }
    }
    onRowClicked( params, resourceName ) {
        if ( !this.config.editable ) {
            let rootComponent = params.context.rootComponent;
            let clickedRowData = params.data;
            let processedResourceName = ( resourceName ) ? resourceName : this.config.resourceName;
            let outputParams: DatagridEventParams = {
                rowId: clickedRowData.id,
                rowData: clickedRowData,
                resourceName: processedResourceName,
                pare: this.config.pare
            }
            rootComponent.rowClick.emit( outputParams );
        }
    }
    onRowDoubleClicked( params, resourceName ) {
        let rootComponent = params.context.rootComponent;
        let clickedRowData = params.data;
        let processedResourceName = ( resourceName ) ? resourceName : this.config.resourceName;
        let outputParams: DatagridEventParams = {
            rowId: clickedRowData.id,
            rowData: clickedRowData,
            resourceName: processedResourceName,
            pare: this.config.pare
        }
        if ( this.config.readOnly || this.config.lovMode ) {
            rootComponent.rowDoubleClick.emit( outputParams );
        } else {
            this.actionEdit.emit( outputParams );
        }
    }
    onRowActionEditClicked() {
        if ( !this.config.readOnly ) {
            let outputParams: DatagridEventParams = {
                rowId: this.hoverRowData.id,
                rowData: this.hoverRowData,
                resourceName: this.config.resourceName,
                pare: this.config.pare
            }
            this.actionEdit.emit( outputParams );
        }
    }
    onRowActionDeleteClicked() {
        if ( !this.config.readOnly ) {
            let outputParams: DatagridEventParams = {
                rowId: this.hoverRowData.id,
                rowData: this.hoverRowData,
                resourceName: this.config.resourceName,
                pare: this.config.pare
            }
            this.actionDelete.emit( outputParams );
        }
    }

    generarColumnDefs(
        columns: DatagridColumn[],
        headerTitle: string,
        resourceName: string,
        details: DatagridConfig,
        resourceConfig?: RestapiResource ): ColGroupDef[] {
        let columnDefs = [];
        let thisEditable = this.config.editable && !this.config.readOnly;
        let thisDetails = this.config.detailConfig;
        let thisReadOnly = this.config.readOnly;
        let thisLovMode = this.config.lovMode;
        let thisRowActionsActive = this.rowActionsActive;
        let gridColumns = columns;
        if ( !columns && resourceConfig ) {
            gridColumns = [];
            resourceConfig.fields.forEach( function( field ) {
                let hidden = ( thisLovMode ) ? field.hiddenInLov : field.hiddenInGrid;
                if ( !hidden ) {
                    gridColumns.push( {
                        field: field.name
                    } );
                }
            } );
        }
        if ( gridColumns ) {
            gridColumns.forEach( function( column: DatagridColumn, index ) {
                let formField;
                if ( resourceConfig ) {
                    resourceConfig.fields.forEach( function( field ) {
                        if ( column.field === field.name ) {
                            formField = field;
                        }
                    } );
                }
                let valueFormatter;
                let cellStyle;
                let cellRenderer;
                let cellRendererFramework;
                let cellEditorFramework;
                let checkboxSelection = false;
                let columnField = column.field;
                if ( formField ) {
                    if ( formField.type === 'DATE' ) {
                        valueFormatter = function( data ) {
                            return moment( data.value ).format( 'DD/MM/YYYY' );
                        }
                    } else if ( formField.type === 'BOOLEAN' ) {
                        valueFormatter = function( data ) {
                            if ( typeof data.value === "string" ) {
                                return ( data.value == 'true' ) ? 'Si' : 'No';
                            } else {
                                return ( data.value ) ? 'Si' : 'No';
                            }
                        }
                        cellEditorFramework = BooleanEditorComponent;
                    } else if ( formField.type === 'BIGDECIMAL' ) {
                        valueFormatter = function( data ) {
                            if ( !data.value && data.value != 0 ) {
                                return '';
                            }
                            let n = 2;
                            let x = 3;
                            let s = '.';
                            let c = ',';
                            let dataValue = ( data.value || data.value == 0 ) ? data.value : 0;
                            let re = '\\d(?=(\\d{' + ( x || 3 ) + '})+' + ( n > 0 ? '\\D' : '$' ) + ')',
                                num = Number( dataValue ).toFixed( Math.max( 0, ~~n ) );
                            return ( ( c ? num.replace( '.', c ) : num ).replace( new RegExp( re, 'g' ), '$&' + ( s || ',' ) ) );
                        }
                        cellStyle = { textAlign: "right" };
                        cellEditorFramework = NumericEditorComponent;
                    } else if ( formField.type === 'LOV' ) {
                        columnField = column.field + '.' + formField.lovDetailFieldName;
                    }
                }
                if ( index == 0 ) {
                    checkboxSelection = true;
                    if ( thisDetails ) {
                        cellRenderer = 'agGroupCellRenderer';
                    }
                }
                if ( index == gridColumns.length - 1 && thisRowActionsActive) {
                    cellRendererFramework = DatagridActionsRendererComponent;
                }
                columnDefs.push( {
                    field: columnField,
                    headerName: column.headerName ? column.headerName : column.field,
                    sortable: true,
                    editable: ( thisEditable ) ? column['editable'] : false,
                    valueFormatter: valueFormatter,
                    cellStyle: cellStyle,
                    cellRenderer: cellRenderer,
                    cellRendererFramework: cellRendererFramework,
                    cellEditorFramework: cellEditorFramework,
                    checkboxSelection: ( !thisReadOnly && !thisLovMode ) ? checkboxSelection : false,
                    suppressMenu: true,
                    filter: false,
                    suppressSizeToFit: ( column.width ) ? true : false,
                    suppressSorting: ( column.sortable === false ) ? true : false,
                    width: ( column.width ) ? column.width : null
                } );
            } );
        }
        if ( this.rowActionsActive ) {
            // Add row actions column
            /*columnDefs.push( {
                cellRendererFramework: DatagridActionsRendererComponent,
                sortable: false,
                width: 80
            } );*/
        }
        if ( this.config.showTopBar ) {
            return columnDefs;
        } else {
            return <ColGroupDef[]>[{
                headerGroupComponentFramework: DatagridHeaderComponent,
                headerGroupComponentParams: {
                    headerTitle: headerTitle,
                    resourceName: resourceName,
                    lovMode: this.config.lovMode,
                    details: details,
                    isStaticData: this.staticData != null,
                    readOnly: this.config.readOnly,
                    editable: this.config.editable,
                    rootComponent: this
                },
                children: columnDefs
            }];
        }
    }

    detailConfigureParams(
        options: any,
        details: DatagridConfig,
        nivell: number = 0 ) {
        let thisOnRowDoubleClicked = this.onRowDoubleClicked;
        let detailDataAttribute = details.dataAttribute;
        let detailGridOptions = {
            suppressCellSelection: true,
            suppressContextMenu: true,
            rowSelection: "multiple",
            onGridReady: function( params ) {
                params.api.sizeColumnsToFit();
                params.api.setGridAutoHeight( true );
            },
            onRowDoubleClicked: function( params ) {
                thisOnRowDoubleClicked( params, details.resourceName );
            },
            columnDefs: this.generarColumnDefs(
                details.columns,
                details.headerTitle,
                details.resourceName,
                details ),
            context: {}
        }
        options.masterDetail = true;
        options.detailCellRendererParams = function( params ) {
            Object.assign( detailGridOptions.context, {
                parePk: params.data.pk
            } );
            let detailParams = {
                detailGridOptions: detailGridOptions,
                getDetailRowData: function( params ) {
                    params.successCallback( params.data[detailDataAttribute] );
                }
            };
            return detailParams;
        }
        if ( details.resourceName ) {
            this.resourceService.getForm().subscribe(( resourceConfig: RestapiResource ) => {
                let colDefs = this.generarColumnDefs(
                    details.columns,
                    details.headerTitle,
                    details.resourceName,
                    details,
                    resourceConfig );
                detailGridOptions.columnDefs = colDefs;
            } );
        }
        options.getRowHeight = this.detailGetRowHeight;
        options.columnDefs[0]['cellRenderer'] = "agGroupCellRenderer";
        let dataContextAfegir = {
            nivell: nivell,
            detailDataAttribute: detailDataAttribute,
            resourceName: details.resourceName,
            rootOptions: this.gridOptions,
            detailRecursiu: this.detailRecursiu,
            rootComponent: this
        }
        Object.assign( options.context, dataContextAfegir );
        options.isRowMaster = function( dataItem ) {
            return ( dataItem && dataItem[detailDataAttribute] ) ? dataItem[detailDataAttribute].length > 0 : false;
        };
        options.onRowGroupOpened = this.detailRowGroupOpened;
        if ( details.detailConfig ) {
            this.detailConfigureParams(
                detailGridOptions,
                details.detailConfig,
                nivell + 1 );
        }
    }

    generarDataSource() {
        return {
            getRows: ( params: any ) => {
                this.gridOptions.api.showLoadingOverlay();
                var sortString;
                var sortState = this.gridOptions.api.getSortModel();
                if ( sortState.length !== 0 ) {
                    sortString = sortState[0].colId + "," + sortState[0].sort;
                }
                var filterState = this.gridOptions.api.getFilterModel();
                var filterSend = [];
                for ( var k in filterState ) {
                    if ( filterState.hasOwnProperty( k ) ) {
                        filterSend.push( { column: k, filter: filterState[k].filter, type: filterState[k].type } );
                    }
                }
                let size = params.endRow - params.startRow;
                let page = params.startRow / size;
                let requestParams = [{
                    key: 'page',
                    value: '' + page
                }];
                params.sortModel.forEach( function( sortModel ) {
                    requestParams.push( {
                        key: 'sort',
                        value: sortModel.colId + ',' + sortModel.sort
                    } );
                } );
                if ( this.filtre && this.filtre.length ) {
                    requestParams.push( {
                        key: 'filtre',
                        value: this.filtre
                    } );
                }
                if ( this.config.pare ) {
                    for ( var property in this.config.pare ) {
                        if ( this.config.pare.hasOwnProperty( property ) ) {
                            requestParams.push( { key: property, value: this.config.pare[property] } );
                        }
                    }
                }
                this.resourceService.getAll( {
                    size: size,
                    params: requestParams
                } ).subscribe(( resources: any ) => {
                    this.gridOptions.api.hideOverlay();
                    params.successCallback(
                        resources,
                        this.resourceService.resourceArray.totalElements );
                    this.actualitzarNumElements(
                        this.resourceService.resourceArray.totalElements,
                        this.gridOptions.api );
                    this.messageService.sendSelection(
                        this.gridOptions.api.getSelectedRows() );
                }, error => {
                    // Suposarem que l'error és causat per no retornar cap resultat
                    this.gridOptions.api.hideOverlay()
                } );
            }
        }
    }

    detailGetRowHeight( params ) {
        let rowHeight = 48;
        let emptyGridHeight = 112;
        let noRowsHeight = 95;
        let oneRowAdditionalHeight = 23;
        let padding = 10;
        if ( params.node && params.node.detail ) {
            let rowCount = 0;
            if ( params.data[params.context.detailDataAttribute] ) {
                rowCount = params.data[params.context.detailDataAttribute].length;
            }
            let alcada = ( rowCount > 0 ) ? rowCount * rowHeight + emptyGridHeight : noRowsHeight;
            if ( rowCount == 1 ) {
                alcada += oneRowAdditionalHeight;
            }
            alcada += 2 * padding;
            return alcada;
        } else {
            return rowHeight;
        }
    }

    detailRowGroupOpened( event ) {
        event.context.detailRecursiu(
            event.context.rootOptions.api,
            0,
            event.data,
            event.context.detailRecursiu );
        event.context.rootOptions.api.onRowHeightChanged();
        event.context.rootOptions.api.checkGridSize()
    }

    detailRecursiu( api, nivell, data, detailRecursiu ) {
        let alcadaPerSumar = 0;
        api.forEachDetailGridInfo( detailGridInfo => {
            let rowId = detailGridInfo.id.split( "_" )[1];
            let detailNode = api.getRowNode( rowId ).detailNode;
            let coincideix = JSON.stringify( detailNode.data ) === JSON.stringify( data );
            if ( coincideix ) {
                let processat = detailNode.data.datagridDetailProcessat;
                if ( !processat ) {
                    detailNode.data.datagridDetailProcessat = true;
                    alcadaPerSumar = detailNode.rowHeight;
                }
            }
            if ( alcadaPerSumar == 0 ) {
                alcadaPerSumar = detailRecursiu(
                    detailGridInfo.api,
                    nivell + 1,
                    data,
                    detailRecursiu );
                if ( alcadaPerSumar > 0 ) {
                    detailNode.setRowHeight( detailNode.rowHeight + alcadaPerSumar );
                    detailGridInfo.api.onRowHeightChanged();
                }
            }
        } );
        return alcadaPerSumar;
    }

    actualitzarNumElements( numElements: number, gridApi?) {
        this.numElements = numElements;
        if ( gridApi ) {
            this.firstRow = ( gridApi ) ? gridApi.getFirstDisplayedRow() : 0;
            this.lastRow = ( gridApi ) ? this.gridOptions.api.getLastDisplayedRow() : 0;
        } else {
            this.firstRow = 0;
            this.lastRow = numElements - 1;
        }
        this.messageService.sendPaginacioInfo( {
            numElements: this.numElements,
            firstRow: this.firstRow,
            lastRow: this.lastRow,
        } );
    }

    @HostListener( 'window:resize', ['$event'] )
    onWindowResize( event ) {
        this.gridOptions.api.sizeColumnsToFit();
    }

    constructor(
        private resourceService: RestapiService,
        private messageService: DatagridMessageService,
        private sanitizer: DomSanitizer ) {
        this.filtreSubscription = this.messageService.getTextFiltre().subscribe(
            textFiltre => {
                this.filtre = textFiltre;
                this.gridOptions.api.setDatasource( this.generarDataSource() );
            } );
    }

}