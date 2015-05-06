
/**
 * INVOICE SERVICE
 * 
 * @author Dženan
 */

'use strict';

var invoiceService = angular.module('InvoiceService', []);

invoiceService.factory('InvoiceProxy', ['$http', '$location',
    function($http, $location) {
        var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/invoice';
        
        return {
            create: function(invoice) {
                return $http.post(url + '/create', invoice);
            }
        };
    }
]);

