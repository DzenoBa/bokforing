'use strict';

/* 
 * Services 
 * 
 * @author Dženan
 */

var ddService = angular.module('DDService', []);

ddService.factory('DDProxy', ['$http',
    function($http) {
       var url = 'http://localhost:8080/bokforing/dd';
        return {
            set: function() {
                return $http.get(url + '/set');
            }
        };
    }]);
