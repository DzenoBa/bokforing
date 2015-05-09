
/**
 * REPORT CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var reportControllers = angular.module('ReportControllers', ['ui.bootstrap']);

reportControllers.controller('ReportCtrl', ['$scope', 'ReportProxy', '$filter', '$http',
    function($scope, ReportProxy, $filter, $http) {
                
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
                        if(angular.isDefined(form.errors.destination)) {
                            // TODO
                            /*$http.get(form.errors.destination, {responseType: 'arraybuffer'})
                                .success(function (data) {
                                    var file = new Blob([data], {type: 'application/pdf'});
                                    var fileURL = URL.createObjectURL(file);
                                    window.open(fileURL);
                            });*/
                            form.numErrors = 0;
                            $scope.form = form;
                        }
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