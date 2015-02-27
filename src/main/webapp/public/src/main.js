require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        'app',
        'controllers',
        'controllers/authControllers',
        'services',
        'services/authServices'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });