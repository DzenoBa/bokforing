require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        '../libs/bootstrap/js/ui-bootstrap-tpls-0.12.1.min',
        '../libs/chartjs/Chart.min',
        'app',
        'controllers/indexControllers',
        'controllers/userControllers',
        'controllers/authControllers',
        'controllers/bookkeepingControllers',
        'controllers/defaultDataController',
        'controllers/productControllers',
        'services/userServices',
        'services/authServices',
        'services/authHandler',
        'services/bookkeepingService',
        'services/defaultDataService',
        'services/productService',
        'services/statisticsService'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });