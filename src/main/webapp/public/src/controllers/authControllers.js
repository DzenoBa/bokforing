
/**
 * AUTH CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var authControllers = angular.module('AuthControllers', []);

authControllers.controller('LoginCtrl', ['$scope',
    '$location', 'AuthProxy',
    function($scope, $location, AuthProxy) {
        $scope.login = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                AuthProxy.login($scope.user)
                        .success(function(form) {
                            if(form.numErrors === 0) {
                                // UPDATE THE SESSION
                                AuthProxy.class().getAuthentication().then(function(value) {
                                    $location.path('/userpage');
                                });                                
                            } else {
                                $scope.form = form;
                            }
                        }).error(function() {
                    console.log("login: error");
                });
            }
        };
    }
]);

authControllers.controller('UserPageCtrl', ['$scope', '$location', 'AuthProxy', 'Session',
    function($scope, $location, AuthProxy, Session) {
        var init = function() {
            $scope.session = AuthProxy.class().getSession();
        };

        $scope.logout = function() {
            AuthProxy.logout()
                    .success(function(boolean) {
                        Session.destroy();
                        $location.path('/login');
                    }).error(function() {
                console.log("logout: error");
            });
        };
        
        init();
    }
]);