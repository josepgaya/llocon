import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CreateComponent } from './create.component';
import { RecoverComponent } from './recover.component';
import { ValidateComponent } from './validate.component';

const routes: Routes = [{
    path: 'create',
    component: CreateComponent
}, {
    path: 'validate/:token',
    component: ValidateComponent
}, {
    path: 'recover',
    component: RecoverComponent
}];

@NgModule( {
    imports: [RouterModule.forChild( routes )],
    exports: [RouterModule]
} )
export class RegistreRoutingModule { }
