
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

        $scope.post = {debit: {0: 0, 1: 0, length:2}, kredit: {0: 0, 1: 0, length:2}};
        
        $scope.sumDebit = function(){
            return sumDebit();
        };
        
        $scope.sumKredit = function() {
            return sumKredit();
        };
        
        function sumDebit() {
            var total = 0;
            for(var i = 0; i < $scope.post.debit.length; i++){
                var debit = $scope.post.debit[i];
                total += debit;
            }
            return total;
        }
        
        function sumKredit() {
            var total = 0;
            for(var i = 0; i < $scope.post.kredit.length; i++){
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
            console.log("jippy");
        };
    }
]);