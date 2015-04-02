
/**
 * AUTH RESOLVER - SERVICE
 * 
 * @author Dženan
 */

'use strict';

var authResolver = angular.module('AuthHandlerService', []);

authResolver.factory('AuthHandler', ['$q', '$location', 'AuthProxy', 'USER_LEVELS',
    function($q, $location, AuthProxy, USER_LEVELS) {
        var functions = {};
        
        functions.redirect = function(auth) {
            if(angular.equals(auth, USER_LEVELS.guest)) {
                $location.path("/login");
            } else if(angular.equals(auth, USER_LEVELS.user)) {
                $location.path("/userpage");
            } else {
                $location.path("/");
            }
        };
        
        return {
            promise: function() {
                var dfrd = $q.defer();
                var level_before = AuthProxy.class().getSession().level;
                AuthProxy.class().getAuthentication().then(function(session) {
                    if(!angular.isDefined(level_before) || angular.equals(session.level, level_before)) {
                        dfrd.resolve();
                    } else {
                        // SOMETHING WENT WRONG REJECT
                        dfrd.reject();
                        functions.redirect(session.level);
                    }
                });
                return dfrd.promise;
            },
            redirect: function(auth) {
                functions.redirect(auth);
            }
        };
    }
]);


