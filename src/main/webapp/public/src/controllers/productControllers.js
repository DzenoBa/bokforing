
/**
 * PRODUCT CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var productControllers = angular.module('ProductControllers', []);

productControllers.controller('ProductCtrl', ['$scope', 'ProductProxy',
    function($scope, ProductProxy) {
        
        $scope.create = function() {
            ProductProxy.create($scope.product)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.product = {price: 0};
                        }
                    }).error(function() {
                        console.log("product:create: error");
                    });
        };
        
        function getQuantityTypes() {
            ProductProxy.getQuantityTypes()
                    .success(function(quantityTypes) {
                        $scope.quantityTypes = quantityTypes;
                    }).error(function() {
                        console.log("product:getQuantityTypes: error");
                    });
        };
        
        // INIT
        getQuantityTypes();
    }
]);