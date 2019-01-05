import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { LloguerListComponent } from "./pages/lloguer-list.component";
import { LloguerDetailComponent } from "./pages/lloguer-detail.component";

const routes: Routes = [{
    path: "",
    data: {
        title: "Inici"
    },
    children: [{
        path: "",
        component: LloguerListComponent,
        data: {
            title: ""
        }
    }, {
        path: ":id/detail",
        component: LloguerDetailComponent,
        data: {
            title: "Detall del lloguer"
        }
    }]
}];

@NgModule( {
    imports: [RouterModule.forChild( routes )],
    exports: [RouterModule]
} )
export class IniciRoutingModule { }
