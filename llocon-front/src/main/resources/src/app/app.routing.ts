import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BngAuthGuard } from 'base-angular';

export const routes: Routes = [{
    path: '',
    loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule),
    canActivate: [BngAuthGuard]
}, {
    path: 'login',
    loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule)
}, {
    path: 'registre',
    loadChildren: () => import('./pages/registre/registre.module').then(m => m.RegistreModule)
}, {
    path: 'usuaris',
    loadChildren: () => import('./pages/usuaris/usuaris.module').then(m => m.UsuarisModule),
    canActivate: [BngAuthGuard]
}, {
    path: '**',
    redirectTo: ''
}];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, { enableTracing: false })],
    declarations: [],
    exports: [
        RouterModule]
})
export class AppRoutingModule { }
