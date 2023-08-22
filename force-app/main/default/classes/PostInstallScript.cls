global class PostInstallScript implements InstallHandler {
    global void onInstall(InstallContext context) {
        if (context.isUpgrade()) {
            System.debug('package 1st demo upgraded. from: ' + context.previousVersion());
        } else {
            System.debug('package 1st demo installed.');
        }
    }
}