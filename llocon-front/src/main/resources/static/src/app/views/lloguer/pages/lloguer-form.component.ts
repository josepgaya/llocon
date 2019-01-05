import { Component, Injector } from "@angular/core";

@Component( {
    template: `
<restapi-form
    restapi-mant
    resourceName="lloguer">
    <!--restapi-custom fieldName="codi"></restapi-custom>
    <restapi-custom fieldName="nom"></restapi-custom>
    <restapi-custom fieldName="adressa"></restapi-custom-->
</restapi-form>`,
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
export class LloguerFormComponent {

}