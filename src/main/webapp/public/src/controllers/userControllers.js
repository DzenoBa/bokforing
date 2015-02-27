
/**
 * USER CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var userControllers = angular.module('UserControllers', []);

userControllers.controller('DDCtrl', ['$scope', 'DDProxy',
    function($scope, DDProxy) {
        $scope.set = function() {
            DDProxy.set()
                    .success(function(form) {
                        $scope.form = form;
                    }).error(function() {
                console.log("user:create: error");
            });
        };
    }]);

userControllers.controller('EditUserCtrl', ['$scope', '$location', 'AuthProxy',
    function($scope, $location, AuthProxy) {
        var init = function() {
            checkLogin();
        };
        
        function checkLogin() {
            AuthProxy.get()
                    .success(function(user) {
                        if (angular.isObject(user)) {
                            $scope.session = {status: "online", user: user};
                        } else {
                            $scope.session = {status: "offline"};
                        }
                    }).error(function() {
                console.log("check: error");
            });
        };
        
        init();
    }]);