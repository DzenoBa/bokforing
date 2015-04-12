
/* 
 * PRODUCT SERVICE
 * 
 * @author Dženan
 */

'use strict';

var productService = angular.module('ProductService', []);

productService.factory('ProductProxy', ['$http', '$location',
    function($http, $location) {
       var url = '//localhost:'+$location.port()+'/bokforing/product';
        return {
            create: function(product) {
                return $http.post(url + '/create', product);
            },
            getQuantityTypes: function() {
                return $http.get(url + '/getquantitytypes');
            },
            getProducts: function(start) {
                return $http.post(url + '/getproducts', start);
            },
            countProducts: function() {
                return $http.get(url + '/countproducts');
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