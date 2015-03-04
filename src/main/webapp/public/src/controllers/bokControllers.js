
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
        }
    }
]);