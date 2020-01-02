
export abstract class LocaleService {

    public static defaultLocaleId = 'es-ES';
    public static implementedLocales = ['ca', 'es', LocaleService.defaultLocaleId];

    public static getCurrentLocale(): string {
        let partialLocaleMatch = null;
        // tslint:disable-next-line:forin
        for ( const id in LocaleService.implementedLocales ) {
            const implemetedLocaleId = LocaleService.implementedLocales[id];
            if ( navigator.language === implemetedLocaleId ) {
                // Exact match, return.
                return implemetedLocaleId;
            } else if ( navigator.language.startsWith( implemetedLocaleId ) ) {
                // For example, browser has `es-CL` and the implemented locale is `es`.
                partialLocaleMatch = implemetedLocaleId;
            } else if ( implemetedLocaleId.startsWith( navigator.language ) ) {
                // For example, browser has `es` and the implemented locale is `es-CL`.
                partialLocaleMatch = implemetedLocaleId;
            }
        }
        if ( partialLocaleMatch != null ) {
            return partialLocaleMatch;
        }
    }

}
