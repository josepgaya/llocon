import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { LloguerGridComponent } from "./pages/lloguer-grid.component";
import { LloguerFormComponent } from "./pages/lloguer-form.component";
import { SubministramentFormComponent } from "./pages/subministrament-form.component";

const routes: Routes = [{
    path: "",
    data: {
        title: "Lloguers"
    },
    children: [{
        path: "",
        component: LloguerGridComponent,
        data: {
            title: ""/*,
            stickyKey: 'lloguerGrid'*/
        }
    }, {
        path: "create",
        component: LloguerFormComponent,
        data: {
            title: "Crear lloguer"
        }
    }, {
        path: ":id/update",
        component: LloguerFormComponent,
        data: {
            title: "Modificar lloguer"
        }
    }, {
        path: ":parentId/update/subministrament/create",
        component: SubministramentFormComponent,
        data: {
            title: "Crear subministrament"
        }
    }, {
        path: ":parentId/update/subministrament/:id/update",
        component: SubministramentFormComponent,
        data: {
            title: "Modificar subministrament"
        }
    }]
}];

@NgModule( {
    imports: [RouterModule.forChild( routes )],
    exports: [RouterModule]
} )
export class LloguerRoutingModule { }
