import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { ConnexioGridComponent } from "./pages/connexio-grid.component";
import { ConnexioFormComponent } from "./pages/connexio-form.component";

const routes: Routes = [{
    path: "",
    data: {
        title: "Connexions"
    },
    children: [{
        path: "",
        component: ConnexioGridComponent,
        data: {
            title: ""/*,
            stickyKey: 'connexioGrid'*/
        }
    }, {
        path: "create",
        component: ConnexioFormComponent,
        data: {
            title: "Crear connexió"
        }
    }, {
        path: ":id/update",
        component: ConnexioFormComponent,
        data: {
            title: "Modificar connexió"
        }
    }]
}];

@NgModule( {
    imports: [RouterModule.forChild( routes )],
    exports: [RouterModule]
} )
export class ConnexioRoutingModule { }
