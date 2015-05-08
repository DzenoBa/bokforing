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
        'controllers/customerControllers',
        'controllers/timesheetController',
        'controllers/invoiceControllers',
        'controllers/reportControllers',
        'services/userServices',
        'services/authServices',
        'services/authHandler',
        'services/bookkeepingService',
        'services/defaultDataService',
        'services/productService',
        'services/customerService',
        'services/statisticsService',
        'services/timesheetService',
        'services/invoiceService',
        'services/reportService'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });