
/**
 * BOOKKEEPING SERVICE
 * 
 * @author Dženan
 */

'use strict';

var bookkeepingService = angular.module('BookkeepingService', []);

bookkeepingService.factory('BookkeepingProxy', ['$http',
    function($http) {
        var url = 'http://localhost:8080/bokforing/bookkeeping';
        
        return {
            createManBook: function(verification) {
                return $http.post(url + '/createman', verification);
            }
        };
    }
]);

