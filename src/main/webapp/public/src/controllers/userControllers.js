
/**
 * USER CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var userControllers = angular.module('UserControllers', []);

userControllers.controller('DDCtrl', ['$scope', 'UserProxy',
    function($scope, UserProxy) {
        $scope.set = function() {
            UserProxy.set()
                    .success(function(form) {
                        $scope.form = form;
                    }).error(function() {
                console.log("user:create: error");
            });
        };
    }]);

userControllers.controller('EditUserCtrl', ['$scope', '$location', 'AuthProxy',
    'UserProxy',
    function($scope, $location, AuthProxy, UserProxy) {
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
        
        $scope.edit = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                UserProxy.edit($scope.user)
                        .success(function(form) {
                            if(form.numErrors === 0) {
                                $scope.form = form;
                                $scope.user = null;
                            } else {
                                $scope.form = form;
                            }
                        }).error(function() {
                    console.log("edit: error");
                });
            }
        };
        
        init();
    }]);