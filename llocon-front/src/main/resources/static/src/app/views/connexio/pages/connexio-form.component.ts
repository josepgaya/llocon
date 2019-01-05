import { Component, Injector } from "@angular/core";

@Component( {
    template: `
<restapi-form
    restapi-mant
    resourceName="connexio"></restapi-form>`,
    styles: [`
#formulari {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}
restapi-custom {
    flex: 0 0 calc(50% - .5em);
    margin: 0;
}
`]
} )
export class ConnexioFormComponent {

}