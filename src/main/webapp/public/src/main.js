require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        'app',
        'controllers/userControllers',
        'controllers/authControllers',
        'services/userServices',
        'services/authServices'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });