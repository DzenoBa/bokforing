
/* 
 * CUSTOMER SERVICE
 * 
 * @author Dženan
 */

'use strict';

var customerService = angular.module('CustomerService', []);

customerService.factory('CustomerProxy', ['$http', '$location',
    function($http, $location) {
       //var url = '//localhost:'+$location.port()+'/bokforing/customer';
       var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/auth';
        return {
            create: function(customer) {
                return $http.post(url + '/create', customer);
            },
            getCustomers: function(customer) {
                return $http.post(url + '/getcustomers', customer);
            },
            countCustomers: function(customer) {
                return $http.post(url + '/countcustomers', customer);
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