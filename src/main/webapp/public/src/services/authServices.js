
/**
 * AUTH SERVICES
 * 
 * @author Dženan
 */

'use strict';

var authService = angular.module('AuthService', []);

authService.factory('AuthProxy', ['$http', '$q','Session', '$location', 'USER_LEVELS',
    function($http, $q, Session, $location, USER_LEVELS) {
        //var url = '//localhost:'+$location.port()+'/bokforing/auth';
        var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/auth';
        var authService = {};
        
        /**
         * GET AUTHENTICATION
         * GETS THE SESSION FROM SERVER
         * @returns {$q@call;defer.promise}
         */
        authService.getAuthentication = function() {
            var dfrd = $q.defer();
            $http.get(url + '/getauthentication')
                    .success(function(user) {
                        if(angular.isDefined(user)) {
                            if(angular.isDefined(user.email) && angular.isDefined(user.level)) {
                                if(user.email !== null && user.level !== null) {
                                    var level;
                                    if(angular.equals(user.level, "Admin")) {
                                        level = USER_LEVELS.admin;
                                    } else if(angular.equals(user.level, "User")) {
                                        level = USER_LEVELS.user;
                                    } else {
                                        level = USER_LEVELS.guest;
                                    }
                                    Session.create(user.email, level);
                                } else {
                                    Session.destroy();
                                }
                            }
                        }
                        return dfrd.resolve(Session);
                    }).error(function() {
                        console.log("error: getauthentication")
                        dfrd.reject();
                    });
            return dfrd.promise;
        };
        
        authService.isAuthorized = function(level) {
            if(angular.equals(level, USER_LEVELS.all)) {
                return true;
            } else {
                return angular.equals(level, Session.level);
            }
        };
        
        authService.getSession = function() {
            return Session;
        };
        
        return {
            login: function(user) {
                return $http.post(url + '/login', user);
            },
            status: function() {
                return $http.get(url + '/status');
            },
            get: function() {
                return $http.get(url + '/get');
            },
            logout: function() {
                return $http.get(url + '/logout');
            },
            class: function() {
                return authService;
            }
        };
    }
]);

authService.service('Session', function(USER_LEVELS) {
    this.create = function(email, level) {
        this.email = email;
        this.level = level;
    };
    
    this.destroy = function() {
        this.email = null;
        this.level = USER_LEVELS.guest;
    };
});


authService.constant('USER_LEVELS', {
  all: '*',
  admin: 'admin',
  user: 'user',
  guest: 'guest'
});

