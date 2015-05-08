
/**
 * REPORT SERVICE
 * 
 * @author Dženan
 */

'use strict';

var reportService = angular.module('ReportService', []);

reportService.factory('ReportProxy', ['$http', '$location',
    function($http, $location) {
        var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/report';
        
        return {
            balancesheet: function(report) {
                return $http.post(url + '/balancesheet', report);
            }
        };
    }
]);



