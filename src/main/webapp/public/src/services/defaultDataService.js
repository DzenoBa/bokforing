
/**
 * DEFUALT-DATA SERVICE
 * 
 * @author Dženan
 */

'use strict';

var defualtDataService = angular.module('DefaultDataService', []);

defualtDataService.factory('DefaultDataProxy', ['$http',
    function($http) {
        var url = 'http://localhost:8080/bokforing/defaultdata';
        
        return {
            initAccounts: function() {
                return $http.get(url + '/accounts');
            }
        };
    }
]);
