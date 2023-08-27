global class PostInstallScript implements InstallHandler {

    @TestVisible
    private Integer responseStatusCode;

    @TestVisible
    private String responseBody;

    global void onInstall(InstallContext context) {
        if (context.isUpgrade()) {
            System.debug('package 1st demo upgraded. from: ' + context.previousVersion());
        } else {
            System.debug('package 1st demo installed.');
        }

        ExternalService.demo_aho.DemoApi api = new ExternalService.demo_aho.DemoApi();

        ExternalService.demo_aho.DemoApi_InstalledRequest requestBody = new ExternalService.demo_aho.DemoApi_InstalledRequest();
        requestBody.organizationId = context.organizationId();
        requestBody.installerId = context.installerId();
        requestBody.isUpgrade = context.isUpgrade();
        requestBody.isPush = context.isPush();
        requestBody.version = versionToString(context.previousVersion());
        
        ExternalService.demo_aho.DemoApi.installed_Request request = new ExternalService.demo_aho.DemoApi.installed_Request();
        request.body = requestBody;
        
        ExternalService.demo_aho.DemoApi.installed_Response response = api.installed(request);
        System.debug(response);

        this.responseStatusCode = response.responseCode;
        this.responseBody = response.Code200;
    }

    private String versionToString(System.Version ver) {
        if (ver == null) {
            return null;
        }

        List<Integer> elements = new List<Integer>();
        elements.add(ver.major());
        elements.add(ver.minor());
        elements.add(ver.patch() == null ? 0 : ver.patch());

        return String.join(elements, '.');
    }
}