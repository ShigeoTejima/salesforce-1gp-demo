global class UninstallScript implements UninstallHandler {

    @TestVisible
    private Integer responseStatusCode;

    @TestVisible
    private String responseBody;

    global void onUninstall(UninstallContext context) {
        System.debug('package 1st demo un-installed.');

        ExternalService.demo_aho.DemoApi api = new ExternalService.demo_aho.DemoApi();

        ExternalService.demo_aho.DemoApi_UninstalledRequest requestBody = new ExternalService.demo_aho.DemoApi_UninstalledRequest();
        requestBody.organizationId = context.organizationId();
        requestBody.uninstallerId = context.uninstallerId();
        
        ExternalService.demo_aho.DemoApi.uninstalled_Request request = new ExternalService.demo_aho.DemoApi.uninstalled_Request();
        request.body = requestBody;
        
        ExternalService.demo_aho.DemoApi.uninstalled_Response response = api.uninstalled(request);
        System.debug(response);

        this.responseStatusCode = response.responseCode;
        this.responseBody = response.Code200;

    }
}