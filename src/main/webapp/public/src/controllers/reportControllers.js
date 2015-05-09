
/**
 * REPORT CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var reportControllers = angular.module('ReportControllers', ['ui.bootstrap']);

reportControllers.controller('ReportCtrl', ['$scope', 'ReportProxy', '$filter',
    function($scope, ReportProxy, $filter) {
                
        $scope.bsDate = $filter('date')(new Date(),'yyyy-MM-dd');
        $scope.isDate = $filter('date')(new Date(),'yyyy-MM-dd');
        
        $scope.balancesheet = function() {
            var request = {start: $scope.bsDate};
            ReportProxy.balancesheet(request)
                    .success(function(form) {
                        $scope.form = form;
                    }).error(function() {
                        console.log("report:balancesheet: error");
                    });
        };
        
        $scope.incomestatement = function() {
            var request = {start: $scope.isDate};
            ReportProxy.incomestatement(request)
                    .success(function(form) {
                        $scope.form = form;
                    }).error(function() {
                        console.log("report:incomestatement: error");
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