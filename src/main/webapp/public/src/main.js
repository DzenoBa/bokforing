require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        '../libs/bootstrap/js/ui-bootstrap-tpls-0.12.1.min',
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