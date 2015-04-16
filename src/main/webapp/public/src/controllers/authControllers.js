
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
        
        function changeprotocol() {
            if(angular.equals($location.$$protocol, 'http')) {
                return true;
            } else {
                return false;
            }
        };
        
        $scope.changeprotocol = changeprotocol();
    }
]);

authControllers.controller('UserPageCtrl', ['$scope', '$location', 'AuthProxy', 'Session',
    function($scope, $location, AuthProxy, Session) {
        var init = function() {
            $scope.session = AuthProxy.class().getSession();
        };

        init();
        
        var data = {
            labels: ["1/1", "3/1", "5/1", "8/1", "20/1", "30/1"],
            datasets: [
                {
                    label: "Label",
                    fillColor: "rgba(151,187,205,0.2)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: [28, 48, 40, 19, 86, 27]
                }
            ]
        };
        var ctx = document.getElementById("myChart").getContext("2d");
        var myLineChart = new Chart(ctx).Line(data);  
    }
]);