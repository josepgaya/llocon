import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { RowNode } from 'ag-grid-community/main';

export class PaginacioInfo {
    numElements: number;
    firstRow: number;
    lastRow: number;
}
/*export class RowChange {
    resourceName: string;
    data: any;
}*/

@Injectable({ providedIn: 'root' })
export class DatagridMessageService {

    private paginacioInfo = new Subject<PaginacioInfo>();
    private textFiltre = new Subject<string>();
    private selection = new Subject<any[]>();
    private hoverRow = new Subject<RowNode>();
    //private rowChange = new Subject<RowChange>();
 
    sendPaginacioInfo(paginacioInfo: PaginacioInfo) {
        this.paginacioInfo.next(paginacioInfo);
    }
    getPaginacioInfo(): Observable<PaginacioInfo> {
        return this.paginacioInfo.asObservable();
    }
    clearPaginacioInfo() {
        this.paginacioInfo.next();
    }

    sendTextFiltre(textFiltre: string) {
        this.textFiltre.next(textFiltre);
    }
    getTextFiltre(): Observable<string> {
        return this.textFiltre.asObservable();
    }
    clearTextFiltre() {
        this.textFiltre.next();
    }

    sendSelection(selection: any[]) {
        this.selection.next(selection);
    }
    getSelection(): Observable<any[]> {
        return this.selection.asObservable();
    }
    clearSelection() {
        this.selection.next();
    }

    sendHoverRow(hoverRow: RowNode) {
        this.hoverRow.next(hoverRow);
    }
    getHoverRow(): Observable<RowNode> {
        return this.hoverRow.asObservable();
    }
    clearHoverRow() {
        this.hoverRow.next();
    }

    /*sendRowChange(rowChange: RowChange) {
        this.rowChange.next(rowChange);
    }
    getRowChange(): Observable<RowChange> {
        return this.rowChange.asObservable();
    }
    clearRowChange() {
        this.rowChange.next();
    }*/

}