import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

export const routes: Routes = [{
    path: 'inici',
    loadChildren: "./views/inici/inici.module#IniciModule"
}, {
    path: "lloguer",
    loadChildren: "./views/lloguer/lloguer.module#LloguerModule"
}, {
    path: "connexio",
    loadChildren: "./views/connexio/connexio.module#ConnexioModule"
}, {
    path: '**',
    redirectTo: 'inici'
}];

@NgModule( {
    imports: [
        RouterModule.forRoot( routes )],
    declarations: [],
    exports: [
        RouterModule]
} )
export class AppRoutingModule { }
