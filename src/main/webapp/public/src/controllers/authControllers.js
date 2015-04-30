
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
                                    $location.path('/start');
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

            // GET REVENUE DATA
            getBalanceList({number: 3}).then(function(data) {
                updateChart(revenueChart, revenueData, data);
            });

            // GET COST DATA
            getBalanceList({number: 4}).then(function(data) {
                updateChart(costChart, costData, data);
            });
        };
        
        function updateChart(chart, chartData, data) {
            var dd = {};
            angular.forEach(data, function(value, key) {
                dd[$filter('date')(key,'dd/MM')] = value;
            });
            for(var i=0; i<7; i++){
                var temp_value = dd[chart.datasets[0].points[i].label];
                chart.datasets[0].points[i].value = temp_value;
                chartData.datasets[0].data[i] = temp_value;
            }
            chart.update();
        }

        function getDates() {
            var dates = [];
            for(var i=6; i>=0; i--) {
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
        
        var revenueData = {
            labels: getDates(),
            datasets: [
                {
                    label: "Assets",
                    fillColor: "rgba(139,204,159,0.9)",
                    strokeColor: "rgba(139,204,159,1)",
                    pointColor: "#fff",
                    pointStrokeColor: "rgba(139,204,159,1)",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(139,204,159,1)",
                    data: [0,0,0,0,0,0,0]
                }
            ]
        };
        var costData = {
            labels: getDates(),
            datasets: [
                {
                    label: "Funds",
                    fillColor: "rgba(240,126,72,0.9)",
                    strokeColor: "rgba(240,126,72,1)",
                    pointColor: "#fff",
                    pointStrokeColor: "rgba(240,126,72,1)",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(240,126,72,1)",
                    data: [0,0,0,0,0,0,0]
                }
            ]
        };
        
        // RENDER CHARTS
        var revenueChart = renderChart('revenueChart', revenueData);
        var costChart = renderChart('costChart', costData);
        
        /*
        * SOMETIMES IF THE USER REFRESHES THE PAGE
        * THE CHART MIGHT BE RENDERED BEFORE THE CSS IS LOADED
        * THIS LEADS TO SIZE PROBLEMS
        * TO RESOLVE THIS: RENDER THE CHARTS AGAIN 
        * WHEN DOCUMENT IS LOADED
        */
        window.onload = function () {
            revenueChart = renderChart('revenueChart', revenueData);
            costChart = renderChart('costChart', costData);
        };
        
        window.onresize = function(event) {
            // RESIZE THE CHARTS WHEN THE WINDOW SIZE CHANGES
            revenueChart = renderChart('revenueChart', revenueData);
            costChart = renderChart('costChart', costData);
        };
        
        /*
         * THIS FUNCTION REMOVES THE OLD CANVAS 
         * AND CREATES A NEW ONE
         * THIS RESOLVES A BUG FOR CHARTJS
         */
        function renderChart(id, data){
            var $canvas = $('#' + id);
            var $parent = $canvas.parent(); 
            $canvas.remove();
            var width = $parent.width();
            var height = width/2;
            $parent.prepend("<canvas width='" + width + "' height='" + height + "' id='" + id + "'>");

            var ctx = $parent.find('#' + id).get(0).getContext("2d");
            return new Chart(ctx).Line(data, {
                animation: false
            });
        }
        
        init();
    }
]);