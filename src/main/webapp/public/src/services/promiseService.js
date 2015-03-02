
/**
 * PROMISE SERVICE
 * 
 * @author Dženan
 */

'use strict';

var promiseService = angular.module('PromiseService', []);

promiseService.factory('PromiseProxy', ['$q',
    function($q) {
        var d = $q.defer();
        return {
            resolve: function() {
                d.resolve();
            },
            promise: function() {
                return d.promise;
            },
            refresh: function() {
                d = $q.defer();
            }
        };
    }
]);


