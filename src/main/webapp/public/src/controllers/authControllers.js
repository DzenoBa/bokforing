
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

authControllers.controller('UserPageCtrl', ['$scope', '$q', '$filter', 'AuthProxy', 'StatisticsProxy',
    function($scope, $q, $filter, AuthProxy, StatisticsProxy) {
        
        var init = function() {
            $scope.session = AuthProxy.class().getSession();
            
            // GET ASSETS DATA
            getBalanceList({number: 1}).then(function(data) {
                var dd = {};
                angular.forEach(data, function(value, key) {
                    dd[$filter('date')(key,'dd/MM')] = value;
                });
                for(var i=0; i<8; i++){
                    assetsChart.datasets[0].points[i].value = dd[assetsChart.datasets[0].points[i].label];
                }
                assetsChart.update();
            });
            
            // GET FUNDS DATA
            getBalanceList({number: 2}).then(function(data) {
                var dd = {};
                angular.forEach(data, function(value, key) {
                    dd[$filter('date')(key,'dd/MM')] = value;
                });
                for(var i=0; i<8; i++){
                    fundsChart.datasets[0].points[i].value = dd[fundsChart.datasets[0].points[i].label];
                }
                fundsChart.update();
            });
        };

        function getDates() {
            var dates = [];
            for(var i=7; i>=0; i--) {
                dates[dates.length] = $filter('date')(new Date() - i * 1000 * 60 * 60 * 24,'dd/MM');
            }
            return dates;
        };
        
        function getBalanceList(account) {
            var dfrd = $q.defer();
            StatisticsProxy.getBalanceList(account)
                    .success(function(data) {
                        dfrd.resolve(data);
                    }).error(function() {
                console.log("getBalanceList: error");
                dfrd.reject();
            });
            return dfrd.promise;
        };
        
        var assetsData = {
            labels: getDates(),
            datasets: [
                {
                    label: "Assets",
                    fillColor: "rgba(139,204,159,1)",
                    strokeColor: "rgba(139,204,159,1)",
                    pointColor: "#fff",
                    pointStrokeColor: "rgba(139,204,159,1)",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(139,204,159,1)",
                    data: [0,0,0,0,0,0,0,0]
                }
            ]
        };
        var fundsData = {
            labels: getDates(),
            datasets: [
                {
                    label: "Funds",
                    fillColor: "rgba(240,126,72,1)",
                    strokeColor: "rgba(240,126,72,1)",
                    pointColor: "#fff",
                    pointStrokeColor: "rgba(240,126,72,1)",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(240,126,72,1)",
                    data: [0,0,0,0,0,0,0,0]
                }
            ]
        };
        var assetsCtx = document.getElementById("assetsChart").getContext("2d");
        var assetsChart = new Chart(assetsCtx).Line(assetsData);
        var fundsCtx = document.getElementById("fundsChart").getContext("2d");
        var fundsChart = new Chart(fundsCtx).Line(fundsData); 
       
        init();
    }
]);