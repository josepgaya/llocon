import { Component, NgZone, OnInit, ViewChild } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { MdcDrawer, MdcList } from '@angular-mdc/web';
import { Subscription, Subject } from 'rxjs';
import { filter } from 'rxjs/operators';

import { AppService } from './core';

@Component( {
    selector: 'app-layout',
    templateUrl: './app-layout.html',
    styles: [`
mdc-top-app-bar {
    z-index: 7;
}
#main-content {
    display: flex;
}
#menu-content {
    display: flex;
    flex-direction: column;
    flex-grow: 0;
}
#right-content {
    flex-grow: 1;
    display: flex;
}
#error-content {
    padding: 1em;
    position: relative;
}
#page-content {
    flex-grow: 1;
}
`]
} )
export class AppLayout implements OnInit {

    @ViewChild( 'appdrawer' ) appdrawer: MdcDrawer;
    @ViewChild( 'menulist' ) menulist: MdcList;

    private menuItems = [
        { name: 'Inici', route: '/inici', icon: 'home' },
        //{ name: 'List', route: '/lloguer-list', icon: 'credit_card' },
        { name: 'Lloguers', route: '/lloguer', icon: 'domain' },
        { name: 'Connexions', route: '/connexio', icon: 'power' }/*,
        { name: 'Subministraments', route: '/subministrament', icon: 'credit_card' },
        { name: 'Articles', route: ['article'], icon: 'credit_card' },
        { name: 'Estudis de projecte', route: ['projecte-estudi'], icon: 'code' }*/
    ];

    ngOnInit(): void {
        this.router.events.pipe(
            filter( event => event instanceof NavigationEnd )
        ).subscribe(( event: NavigationEnd ) => {
            let indexTrobat;
            for ( let i = 0; i < this.menuItems.length; i++ ) {
                if ( event.url.startsWith( this.menuItems[i].route ) ) {
                    indexTrobat = i;
                    break;
                }
            }
            if ( indexTrobat !== undefined ) {
                this.menulist.setSelectedIndex( indexTrobat );
            }
        } );
    }

    isScreenSmall(): boolean {
        return this.appService.isScreenSmall();
    }
    isContextTopbar(): boolean {
        return this.appService.isContextTopBar();
    }

    topBarMenuIconClick() {
        if ( this.appdrawer.open ) {
            this.appdrawer.open = false;
        } else {
            this.appdrawer.open = true;
        }
    }

    constructor(
        private router: Router,
        private ngZone: NgZone,
        private appService: AppService ) {
        appService.setZone( ngZone );
    }

}