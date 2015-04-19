
/* 
 * CUSTOMER SERVICE
 * 
 * @author Dženan
 */

'use strict';

var customerService = angular.module('CustomerService', []);

customerService.factory('CustomerProxy', ['$http', '$location',
    function($http, $location) {
       var url = '//localhost:'+$location.port()+'/bokforing/customer';
        return {
            create: function(customer) {
                return $http.post(url + '/create', customer);
            },
            getCustomers: function(start) {
                return $http.post(url + '/getcustomers', start);
            },
            countCustomers: function() {
                return $http.get(url + '/countcustomers');
            },
            edit: function(customer) {
                return $http.post(url + '/edit', customer);
            },
            delete: function(customer) {
                return $http.post(url + '/delete', customer);
            }
        };
    }
]);