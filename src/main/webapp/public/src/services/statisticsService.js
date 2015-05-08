
/**
 * STATISTICS SERVICE
 * 
 * @author Dženan
 */

'use strict';

var statisticsService = angular.module('StatisticsService', []);

statisticsService.factory('StatisticsProxy', ['$http', '$location',
    function($http, $location) {
        //var url = '//localhost:'+$location.port()+'/bokforing/statistics';
        var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/statistics';
        
        return {
            getBalanceList: function(account) {
                return $http.post(url + '/balancelist', account);
            }
        };
    }
]);

