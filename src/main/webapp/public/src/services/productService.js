
/* 
 * PRODUCT SERVICE
 * 
 * @author Dženan
 */

'use strict';

var productService = angular.module('ProductService', []);

productService.factory('ProductProxy', ['$http', '$location',
    function($http, $location) {
       //var url = '//localhost:'+$location.port()+'/bokforing/product';
       var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/auth';
        return {
            create: function(product) {
                return $http.post(url + '/create', product);
            },
            getQuantityTypes: function() {
                return $http.get(url + '/getquantitytypes');
            },
            getProducts: function(product) {
                return $http.post(url + '/getproducts', product);
            },
            countProducts: function(product) {
                return $http.post(url + '/countproducts', product);
            },
            edit: function(product) {
                return $http.post(url + '/edit', product);
            },
            delete: function(product) {
                return $http.post(url + '/delete', product);
            }
        };
    }
]);