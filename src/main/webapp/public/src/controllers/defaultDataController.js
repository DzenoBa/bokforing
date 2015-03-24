
/**
 * DEFAULT-DATA CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var defaultDataControllers = angular.module('DefaultDataControllers', []);

defaultDataControllers.controller('DefaultDataCtrl', ['$scope', 'DefaultDataProxy',
    function($scope, DefaultDataProxy) {
        
        $scope.accounts = function() {
            DefaultDataProxy.initAccounts()
                    .success(function(form) {
                        $scope.form = form;
                    }).error(function() {
                        console.log("dd:accounts: error");
                    });
        };
    }
]);