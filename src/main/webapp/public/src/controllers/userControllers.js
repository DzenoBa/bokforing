
/**
 * USER CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var userControllers = angular.module('UserControllers', []);

userControllers.controller('RegisterCtrl', ['$scope', 'UserProxy',
    function($scope, UserProxy) {
        $scope.create = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                UserProxy.create($scope.user)
                        .success(function(form) {
                            $scope.form = form;
                        }).error(function() {
                    console.log("user:create: error");
                });
            }
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
        
        $scope.passwdstrength = function(str) {
            // CHECK LENGTH
            if(str.length > 7) {
                $scope.passwdstrength.one = true;
            } else {
                $scope.passwdstrength.one = false;
            }
            // CHECK IF IT CONTAINS NUMBERS
            if(/[0-9]+/.test(str)) {
                $scope.passwdstrength.two = true;
            } else {
                $scope.passwdstrength.two = false;
            }
            // CHECK LOWERCASE AND UPPERCASE LETTERS
            if(/[a-z]+/.test(str) && /[A-Z]+/.test(str)) {
                $scope.passwdstrength.three = true;
            } else {
                $scope.passwdstrength.three = false;
            }
        };
        
        init();
    }]);