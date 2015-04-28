
/* 
 * TIMESHEET SERVICE
 * 
 * @author Dženan
 */

'use strict';

var timesheetService = angular.module('TimesheetService', []);

timesheetService.factory('TimesheetProxy', ['$http', '$location',
    function($http, $location) {
       var url = '//localhost:'+$location.port()+'/bokforing/timesheet';
        return {
            create: function(timesheet) {
                return $http.post(url + '/create', timesheet);
            },
            getTimesheets: function(timesheet) {
                return $http.post(url + '/gettimesheets', timesheet);
            },
            countTimesheets: function() {
                return $http.get(url + '/counttimesheets');
            },
            edit: function(timesheet) {
                return $http.post(url + '/edit', timesheet);
            },
            delete: function(timesheet) {
                return $http.post(url + '/delete', timesheet);
            }
        };
    }
]);