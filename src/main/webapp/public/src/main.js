require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        '//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.12.1.js',
        'app',
        'controllers/userControllers',
        'controllers/authControllers',
        'controllers/bookkeepingControllers',
        'services/userServices',
        'services/authServices',
        'services/promiseService',
        'services/bookkeepingService'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });