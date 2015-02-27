'use strict';

/*
 * Controllers
 * 
 * @author Dženan
 */

var ddControllers = angular.module('DDControllers', []);

ddControllers.controller('DDCtrl', ['$scope', 'DDProxy',
    function($scope, DDProxy) {
        $scope.set = function() {
            DDProxy.set()
                    .success(function(form) {
                        $scope.form = form;
                    }).error(function() {
                console.log("dd:create: error");
            });
        };
    }]);