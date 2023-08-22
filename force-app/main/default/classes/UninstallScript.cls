global class UninstallScript implements UninstallHandler {
    global void onUninstall(UninstallContext ctx) {
        System.debug('package 1st demo un-installed.');
    }
}