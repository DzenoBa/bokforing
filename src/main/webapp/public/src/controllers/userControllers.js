
/**
 * USER CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var userControllers = angular.module('UserControllers', []);

userControllers.controller('RegisterCtrl', ['$scope', 'UserProxy',
    function($scope, UserProxy) {
        $scope.create = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                UserProxy.create($scope.user)
                        .success(function(form) {
                            $scope.form = form;
                            $scope.checkbox.value1 = false; // Reset check box
                            if(form.numErrors === 0) {
                                $scope.user = null;
                            }
                        }).error(function() {
                    console.log("user:create: error");
                });
            }
        };
        
        $scope.passwdstrength = function(str) {
            // CHECK LENGTH
            if(str.length > 7) {
                $scope.passwdstrength.one = true;
            } else {
                $scope.passwdstrength.one = false;
            }
            // CHECK IF IT CONTAINS NUMBERS
            if(/[0-9]+/.test(str)) {
                $scope.passwdstrength.two = true;
            } else {
                $scope.passwdstrength.two = false;
            }
            // CHECK LOWERCASE AND UPPERCASE LETTERS
            if(/[a-z]+/.test(str) && /[A-Z]+/.test(str)) {
                $scope.passwdstrength.three = true;
            } else {
                $scope.passwdstrength.three = false;
            }
        };
    }]);

userControllers.controller('EditUserCtrl', ['$scope', 'UserProxy',
    function($scope, UserProxy) {
        
        $scope.edit = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                UserProxy.edit($scope.user)
                        .success(function(form) {
                            if(form.numErrors === 0) {
                                $scope.form = form;
                                $scope.user = null;
                            } else {
                                $scope.form = form;
                            }
                        }).error(function() {
                    console.log("edit: error");
                });
            }
        };
        
        $scope.passwdstrength = function(str) {
            // CHECK LENGTH
            if(str.length > 7) {
                $scope.passwdstrength.one = true;
            } else {
                $scope.passwdstrength.one = false;
            }
            // CHECK IF IT CONTAINS NUMBERS
            if(/[0-9]+/.test(str)) {
                $scope.passwdstrength.two = true;
            } else {
                $scope.passwdstrength.two = false;
            }
            // CHECK LOWERCASE AND UPPERCASE LETTERS
            if(/[a-z]+/.test(str) && /[A-Z]+/.test(str)) {
                $scope.passwdstrength.three = true;
            } else {
                $scope.passwdstrength.three = false;
            }
        };
    }]);

/**
 * USER INFO
 */
userControllers.controller('UserInfoCtrl', ['$scope', 'UserProxy',
    function($scope, UserProxy) {
              
        var init = function() {
            getUserInfo();
        };
        
        function getUserInfo() {
            UserProxy.getUserInfo()
                    .success(function(userInfo) {
                        $scope.userinfo = userInfo;
                    }).error(function() {
                console.log("getUserInfo: error");
            });
        };
        
        init();
        $scope.showmapedit = createShowMap();
        $scope.userinfoShadow = {};
        
        function createShowMap() {
            var keys = ['firstname', 'lastname', 'phonenumber', 'city', 'address', 'companyname'];
            var output = {};
            for (var key in keys) {
                output[keys[key]] = false;
            }
            return output;
        }

        $scope.showedit = function(str) {
            $scope.showmapedit[str] = true;
            $scope.userinfoShadow[str] = $scope.userinfo[str];
        };
        $scope.canceledit = function(str) {
            $scope.userinfo[str] = $scope.userinfoShadow[str];
            $scope.showmapedit[str] = false;
        };
        
        $scope.edit = function(str) {
            if(!(angular.isUndefined($scope.userinfo[str]) || $scope.userinfo[str] === null)) {
                var temp_object = {};
                temp_object[str] = $scope.userinfo[str];
                UserProxy.editUserInfo(temp_object)
                        .success(function(form) {
                            if(form.numErrors === 0) {
                                $scope.form = form;
                                getUserInfo();
                            } else {
                                $scope.form = form;
                            }
                        }).error(function() {
                    console.log("editUserInfo: error");
                });
            }
            $scope.showmapedit[str] = false;
        };
    }
]);

/**
 * PASSWORD RESET
 */
userControllers.controller('PasswdResetCtrl', ['$scope', 'UserProxy',
    function($scope, UserProxy) {
        $scope.submit = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                UserProxy.passwdReset($scope.user)
                        .success(function(form) {
                            $scope.form = form;
                        }).error(function() {
                    console.log("passwdreset: error");
                });
            }
        };
    }
]);

/**
 * PASSWORD RECOVERY
 */
userControllers.controller('PasswdRecoveryCtrl', ['$scope', '$location', 'UserProxy',
    function($scope, $location, UserProxy) {
        
        var init = function() {
            getEmail();
        };
        
        $scope.user = {};
        $scope.show = false;
        init();
        
        function getEmail() {
            if(!angular.isUndefined($location.search().key)) {
                $scope.user = {accesskey: $location.search().key};
                UserProxy.keyExist($scope.user)
                        .success(function(user) {
                            $scope.user = user;
                            if(!angular.isUndefined($scope.user.email)) {
                                if($scope.user.email !== null) {
                                    $scope.show = true;
                                }
                            }
                        }).error(function() {
                    console.log("keyExist: error");
                });
            }
        };
        
        $scope.submit = function() {
            if(!(angular.isUndefined($scope.user) || $scope.user === null)) {
                $scope.user.accesskey = $location.search().key;
                UserProxy.passwdRecovery($scope.user)
                        .success(function(form) {
                            $scope.form = form;
                            $scope.user = {};
                        }).error(function() {
                    console.log("passwdrecovery: error");
                });
            }
        };
    }
]);