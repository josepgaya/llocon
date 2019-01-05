import { NgZone } from '@angular/core';

const SMALL_WIDTH_BREAKPOINT = 750;

export class AppService {

    private screenSmall: boolean = false;
    private contextTopBar: boolean = false;

    private mediaMatcher: MediaQueryList = matchMedia( `(max-width: ${SMALL_WIDTH_BREAKPOINT}px)` );
    private zone: NgZone;

    public setZone( zone: NgZone ) {
        this.zone = zone;
    }
    public isScreenSmall(): boolean {
        return this.mediaMatcher.matches;
    }
    public setContextTopBar( contextTopBar: boolean ) {
        this.contextTopBar = contextTopBar;
    }
    public isContextTopBar(): boolean {
        return this.contextTopBar;
    }

    constructor() {
        this.mediaMatcher.addListener( mediaMatcher => {
            if (this.zone) {
                this.zone.run(() => {
                    this.mediaMatcher = mediaMatcher;
                } );
            }
        } );
    }

}