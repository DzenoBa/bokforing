
/**
 * TIMESHEETS CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var timesheetControllers = angular.module('TimesheetControllers', ['ui.bootstrap']);

timesheetControllers.controller('TimesheetCtrl', ['$scope', 'TimesheetProxy', 'ProductProxy', '$q', '$modal',
    function($scope, TimesheetProxy, ProductProxy, $q, $modal) {
        
        $scope.currentPage = 1;
        $scope.timesheet = {};
        $scope.searchproductname = "";
        $scope.products = [];
        
        $scope.create = function() {
            TimesheetProxy.create($scope.timesheet)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.product = {};
                            getTimesheets();
                            countTimesheets();
                        }
                    }).error(function() {
                        console.log("timesheet:create: error");
                    });
        };
        
        function getTimesheets() {
            var index = $scope.currentPage-1;
            TimesheetProxy.getTimesheets({startrange: index})
                    .success(function(timesheets) {
                        $scope.timesheets = timesheets;
                    }).error(function() {
                        console.log("getTimesheets: error");
                    });
        };
        
        function countTimesheets() {
            TimesheetProxy.countTimesheets()
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("countTimesheets: error");
                    });
        };
        
        $scope.edit = function() {
            TimesheetProxy.edit($scope.timesheet)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            getTimesheets();
                        }
                    }).error(function() {
                        console.log("edit: error");
                    });
        };
        
        $scope.delete = function() {
            TimesheetProxy.delete({id: $scope.timesheet.id})
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.timesheet = {};
                            getTimesheets();
                            countTimesheets();
                        }
                    }).error(function() {
                        console.log("delete: error");
                    });
        };
        
        // INIT
        getTimesheets();
        countTimesheets();
        
        $scope.openproducts = function () {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/productSelecterModal.html',
                controller: 'ModalInstanceProductCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedProduct) {
                $scope.timesheet.product = {number: selectedProduct.id, name: selectedProduct.name};
            });
        };
        
        $scope.opencustomers = function () {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/customerSelecterModal.html',
                controller: 'ModalInstanceCustomerCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedCustomer) {
                $scope.timesheet.customer = {number: selectedCustomer.customernumber, name: selectedCustomer.name};
            });
        };
    }
]);