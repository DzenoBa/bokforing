
/**
 * PRODUCT CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var productControllers = angular.module('ProductControllers', []);

productControllers.controller('ProductCtrl', ['$scope', 'ProductProxy',
    function($scope, ProductProxy) {
        
        $scope.currentPage = 1;
        
        $scope.create = function() {
            ProductProxy.create($scope.product)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.product = {price: 0};
                            getProducts();
                            countProducts();
                        }
                    }).error(function() {
                        console.log("product:create: error");
                    });
        };
        
        $scope.pageChanged = function() {
            getProducts();
        };
        
        function getQuantityTypes() {
            ProductProxy.getQuantityTypes()
                    .success(function(quantityTypes) {
                        $scope.quantityTypes = quantityTypes;
                    }).error(function() {
                        console.log("product:getQuantityTypes: error");
                    });
        };
        
        function getProducts() {
            var stringIndex = $scope.currentPage-1 + "";
            ProductProxy.getProducts(stringIndex)
                    .success(function(products) {
                        $scope.products = products;
                    }).error(function() {
                        console.log("product:getProducts: error");
                    });
        };
        
        function countProducts() {
            ProductProxy.countProducts()
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("product:countProducts: error");
                    });
        };
        
        // INIT
        getProducts();
        countProducts();
        getQuantityTypes();
    }
]);