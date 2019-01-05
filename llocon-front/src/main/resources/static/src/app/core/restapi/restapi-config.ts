export class RestapiConfig {

    private serverUrl = 'http://localhost:8180';
    private contextPath = '/llocon';

    public getServerUrl() {
        return this.serverUrl;
    }
    public getContextPath() {
        return this.contextPath;
    }
    public getServerUrlAmbContext() {
        return this.serverUrl + this.contextPath;
    }

    public getApiServiceUrl(contextRelativeUri: string) {
        return this.getServerUrlAmbContext() + contextRelativeUri;
    }

}