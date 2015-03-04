
/**
 * BOK CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var bokControllers = angular.module('BokControllers', []);

bokControllers.controller('ManBokCtrl', ['$scope',
    function($scope) {
        $scope.rows = 2;
        $scope.getNumber = function(num) {
            return new Array(num);   
        };

        $scope.post = {debit: {0: 0, 1: 0}, kredit: {0: 0, 1: 0}};
        
        $scope.sumDebit = function(){
            return sumDebit();
        };
        
        $scope.sumKredit = function() {
            return sumKredit();
        };
        
        function sumDebit() {
            var total = 0;
            for(var i = 0; i < $scope.rows; i++){
                var debit = $scope.post.debit[i];
                total += debit;
            }
            return total;
        }
        
        function sumKredit() {
            var total = 0;
            for(var i = 0; i < $scope.rows; i++){
                var kredit = $scope.post.kredit[i];
                total += kredit;
            }
            return total;
        }
        
        $scope.refreshSumDebit = function() {
            $scope.sumDebit = function(){
                return sumDebit();
            };
        };
        $scope.refreshSumKredit = function() {
            $scope.sumKredit = function(){
                return sumKredit();
            };
        };
        
        $scope.addRow = function() {
            $scope.post.debit[$scope.rows] = 0;
            $scope.post.kredit[$scope.rows] = 0;
            $scope.rows = $scope.rows+1;
            console.log($scope.post);
        };
        
        $scope.removeRow = function(index) {
            console.log(index);
            $scope.post.debit[index] = null;
            $scope.post.kredit[index] = null;
            $scope.rows = $scope.rows-1;
            console.log($scope.post);
        };
    }
]);