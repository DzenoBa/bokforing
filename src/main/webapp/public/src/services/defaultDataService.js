
/**
 * DEFUALT-DATA SERVICE
 * 
 * @author Dženan
 */

'use strict';

var defualtDataService = angular.module('DefaultDataService', []);

defualtDataService.factory('DefaultDataProxy', ['$http', '$location',
    function($http, $location) {
        //var url = '//localhost:'+$location.port()+'/bokforing/defaultdata';
        var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/auth';
        
        return {
            initAccounts: function() {
                return $http.get(url + '/accounts');
            }
        };
    }
]);
