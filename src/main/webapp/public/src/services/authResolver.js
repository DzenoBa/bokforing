
/**
 * AUTH RESOLVER - SERVICE
 * 
 * @author Dženan
 */

'use strict';

var authResolver = angular.module('AuthResolverService', []);

authResolver.factory('AuthResolver', ['$q', '$location', 'AuthProxy', 'USER_LEVELS',
    function($q, $location, AuthProxy, USER_LEVELS) {
        return {
            promise: function() {
                var dfrd = $q.defer();
                AuthProxy.class().getAuthentication().then(function(value) {
                    dfrd.resolve();
                });
                return dfrd.promise;
            },
            redirect: function(auth) {
                if(angular.equals(auth, USER_LEVELS.guest)) {
                    $location.path("/login");
                } else if(angular.equals(auth, USER_LEVELS.user)) {
                    $location.path("/userpage");
                } else {
                    $location.path("/");
                }
            }
        };
    }
]);


