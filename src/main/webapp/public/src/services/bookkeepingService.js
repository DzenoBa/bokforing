
/**
 * BOOKKEEPING SERVICE
 * 
 * @author Dženan
 */

'use strict';

var bookkeepingService = angular.module('BookkeepingService', []);

bookkeepingService.factory('BookkeepingProxy', ['$http', '$location',
    function($http, $location) {
        var url = '//localhost:'+$location.port()+'/bokforing/bookkeeping';
        
        return {
            createManBook: function(verification) {
                return $http.post(url + '/createman', verification);
            },
            editVerification: function(verification) {
                return $http.post(url + '/editverification', verification);
            },
            searchAccount: function(account) {
                return $http.post(url + '/searchaccount', account);
            },
            countSearchAccount: function(account) {
                return $http.post(url + '/countsearchaccount', account);
            },
            getVerifications: function(start) {
                return $http.post(url + '/getverifications', start);
            },
            countVerifications: function() {
                return $http.get(url + '/countverifications');
            },
            getVerificationsByAccount: function(account) {
                return $http.post(url + '/getverificationsbyaccount', account);
            },
            countVerificationsByAccount: function(account) {
                return $http.post(url + '/countverificationsbyaccount', account);
            }
        };
    }
]);

