import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
//import { LicenseManager } from "ag-grid-enterprise";

if (environment.production) {
  enableProdMode();
}

/*LicenseManager.setLicenseKey(
        "Evaluation_License-_Not_For_Production_Valid_Until_25_May_2019__MTU1ODczODgwMDAwMA==156057ec2a5212d3fc17b2c425718067"
);*/

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
