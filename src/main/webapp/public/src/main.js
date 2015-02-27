require.config({
    baseUrl: '/bokforing/public/src',
    urlArgs: 'v=1.0'
});

require(
    [
        'app',
        'controllers',
        'services'
        
    ],
    function () {
        angular.bootstrap(document, ['Bok']);
    });