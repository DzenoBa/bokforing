
/**
 * INDEX CONTROLLERS
 * 
 * @author Dženan
 */
'use strict';

var indexControllers = angular.module('IndexControllers', []);

indexControllers.controller('IndexCtrl', ['$scope',
    '$location', 'AuthProxy', 'Session',
    function($scope, $location, AuthProxy, Session) {
        
        $scope.authShow = function(level) {
            return AuthProxy.class().isAuthorized(level);
        };

        $scope.page = function(page) {
            return angular.equals($location.path(), '/' + page)
        }
        
        $scope.logout = function() {
            AuthProxy.logout()
                    .success(function(boolean) {
                        Session.destroy();
                        $location.path('/login');
                    }).error(function() {
                console.log("logout: error");
            });
        };
    }
]);