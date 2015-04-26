
/**
 * TIMESHEETS CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var timesheetControllers = angular.module('TimesheetControllers', ['ui.bootstrap']);

timesheetControllers.controller('TimesheetCtrl', ['$scope', 'TimesheetProxy', '$filter', '$modal',
    function($scope, TimesheetProxy, $filter, $modal) {
        
        $scope.currentPage = 1;
        $scope.timesheet = {date:  $filter('date')(new Date(),'yyyy-MM-dd')};
        $scope.createboolean = true;
        $scope.editboolean = false;
        
        $scope.create = function() {
            TimesheetProxy.create($scope.timesheet)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.timesheet = {date:  $filter('date')(new Date(),'yyyy-MM-dd')};;
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
                            $scope.editboolean = false;
                            $scope.deleteboolean = true;
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
        
        $scope.pageChanged = function() {
            getTimesheets();
        };
        
        $scope.showedit = function(customer) {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.createboolean = false;
            $scope.deleteboolean = false;
            $scope.editboolean = true;
            $scope.timesheet = angular.copy(customer);
        };
        
        $scope.showcreate = function() {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.editboolean = false;
            $scope.deleteboolean = false;
            $scope.createboolean = true;
            $scope.timesheet = {date:  $filter('date')(new Date(),'yyyy-MM-dd')};
        };
        
        $scope.openproducts = function () {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/productSelecterModal.html',
                controller: 'ModalInstanceProductCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedProduct) {
                $scope.timesheet.product = {id: selectedProduct.id, name: selectedProduct.name};
            });
        };
        
        $scope.opencustomers = function () {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/customerSelecterModal.html',
                controller: 'ModalInstanceCustomerCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedCustomer) {
                $scope.timesheet.customer = {customernumber: selectedCustomer.customernumber, name: selectedCustomer.name};
            });
        };
        
        $scope.opencal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            
            $scope.opened = true;
        };
        
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            yearRange: 1,
            maxMode: 'month',
            currentText: 'Idag'
        };  
    }
]);