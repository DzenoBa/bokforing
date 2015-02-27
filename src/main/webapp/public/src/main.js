require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        'app',
        'controllers',
        'controllers/authControllers',
        'services'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });