'use strict';

/*
 * Controllers
 * 
 * @author Dženan
 */

var authControllers = angular.module('AuthControllers', []);

authControllers.controller('LoginCtrl', ['$scope',
    '$location', 'AuthProxy',
    function($scope, $location, AuthProxy) {
        $scope.login = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                AuthProxy.login($scope.user)
                        .success(function(form) {
                            if(form.numErrors === 0) {
                                $location.path('/userpage');
                            } else {
                                $scope.form = form;
                            }
                        }).error(function() {
                    console.log("login: error");
                });
            }
        };
    }]);

authControllers.controller('UserPageCtrl', ['$scope', '$location', 'AuthProxy',
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
        
        $scope.logout = function() {
            AuthProxy.logout()
                    .success(function(boolean) {
                        $location.path('/login');
                    }).error(function() {
                console.log("logout: error");
            });
        };
        
        init();
    }]);