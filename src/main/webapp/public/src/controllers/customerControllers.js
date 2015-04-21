
/**
 * CUSTOMER CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var customerControllers = angular.module('CustomerControllers', []);

customerControllers.controller('CustomerCtrl', ['$scope', 'CustomerProxy',
    function($scope, CustomerProxy) {
        
        $scope.currentPage = 1;
        $scope.createboolean = true;
        $scope.editboolean = false;
        
        $scope.create = function() {
            CustomerProxy.create($scope.customer)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.customer = {};
                            getCustomers();
                            countCustomers();
                        }
                    }).error(function() {
                        console.log("customer:create: error");
                    });
        };
        
        $scope.pageChanged = function() {
            getCustomers();
        };
        
        $scope.showedit = function(customer) {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.createboolean = false;
            $scope.deleteboolean = false;
            $scope.editboolean = true;
            $scope.customer = angular.copy(customer);
        };
        
        $scope.showcreate = function() {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.editboolean = false;
            $scope.deleteboolean = false;
            $scope.createboolean = true;
            $scope.customer = {};
        };
 
        function getCustomers() {
            var stringIndex = $scope.currentPage-1 + "";
            CustomerProxy.getCustomers({name: $scope.searchname, startrange: stringIndex})
                    .success(function(customers) {
                        $scope.customers = customers;
                    }).error(function() {
                        console.log("customer:getCustomers: error");
                    });
        };
        
        function countCustomers() {
            CustomerProxy.countCustomers({name: $scope.searchname})
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("customer:countCustomers: error");
                    });
        };
        
        $scope.search = function() {
            getCustomers();
            countCustomers();
        };
        
        $scope.autosearch = function() {
            if($scope.searchname.length > 2 || $scope.searchname.length === 0) {
                $scope.search();
            }
        };
        
        $scope.edit = function() {
            CustomerProxy.edit($scope.customer)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            getCustomers();
                        }
                    }).error(function() {
                        console.log("customer:edit: error");
                    });
        };
        
        $scope.delete = function() {
            CustomerProxy.delete({customernumber: $scope.customer.customernumber})
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.customers = {};
                            $scope.editboolean = false;
                            $scope.deleteboolean = true;
                            getCustomers();
                            countCustomers();
                        }
                    }).error(function() {
                        console.log("customer:delete: error");
                    });
        };
        
        // INIT
        getCustomers();
        countCustomers();
    }
]);