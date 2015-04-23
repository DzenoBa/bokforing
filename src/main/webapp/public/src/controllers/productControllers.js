
/**
 * PRODUCT CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var productControllers = angular.module('ProductControllers', []);

productControllers.controller('ProductCtrl', ['$scope', 'ProductProxy', '$modal',
    function($scope, ProductProxy, $modal) {
        
        $scope.currentPage = 1;
        $scope.createboolean = true;
        $scope.editboolean = false;
        $scope.product = {};
        
        $scope.create = function() {
            if($scope.vatacc === "6")
                $scope.product.vat = {number: 2630};
            else if ($scope.vatacc === "12")
                $scope.product.vat = {number: 2620};
            else if($scope.vatacc === "25")
                $scope.product.vat = {number: 2610};
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
        
        $scope.showedit = function(product) {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.createboolean = false;
            $scope.deleteboolean = false;
            $scope.editboolean = true;
            $scope.product = angular.copy(product);
        };
        
        $scope.showcreate = function() {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.editboolean = false;
            $scope.deleteboolean = false;
            $scope.createboolean = true;
            $scope.product = {price: 0};
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
            ProductProxy.getProducts({name: $scope.searchname, startrange: stringIndex})
                    .success(function(products) {
                        $scope.products = products;
                    }).error(function() {
                        console.log("product:getProducts: error");
                    });
        };
        
        function countProducts() {
            ProductProxy.countProducts({name: $scope.searchname})
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("product:countProducts: error");
                    });
        };
        
        $scope.search = function() {
            getProducts();
            countProducts();
        };
        
        $scope.autosearch = function() {
            if($scope.searchname.length > 2 || $scope.searchname.length === 0) {
                $scope.search();
            }
        };
        
        $scope.edit = function() {
            ProductProxy.edit($scope.product)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            getProducts();
                        }
                    }).error(function() {
                        console.log("product:edit: error");
                    });
        };
        
        $scope.delete = function() {
            ProductProxy.delete({id: $scope.product.id})
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.product = {};
                            $scope.editboolean = false;
                            $scope.deleteboolean = true;
                            getProducts();
                            countProducts();
                        }
                    }).error(function() {
                        console.log("product:delete: error");
                    });
        };
        
        $scope.openaccounts = function () {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/accountSelecterModal.html',
                controller: 'ModalInstanceAccountCtrl',
                size: 'lg',
                resolve: {
                    accountType: function() {
                        return 3;
                    }
                }
            });

            modalInstance.result.then(function (selectedAccount) {
                $scope.product.account = {number: selectedAccount.number, name: selectedAccount.name};
            });
        };
        
        // INIT
        getProducts();
        countProducts();
        getQuantityTypes();
    }
]);