import { NgModule, ErrorHandler } from "@angular/core";
import { AngularHalModule } from 'angular4-hal';

import { AppService } from "./app.service";
import { RestapiConfig } from "./restapi/restapi-config";
import { HalExternalConfiguration } from './hal-external-config';
import { GlobalErrorHandler } from './global-error-handler';

@NgModule( {
    imports: [
        AngularHalModule.forRoot()],
    providers: [
        AppService,
        RestapiConfig,
        { provide: 'ExternalConfigurationService', useClass: HalExternalConfiguration },
        { provide: ErrorHandler, useClass: GlobalErrorHandler }]
} )
export class CoreModule {}