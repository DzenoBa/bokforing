
/**
 * BOK CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var bookkeepingControllers = angular.module('BookkeepingControllers', ['ui.bootstrap']);

bookkeepingControllers.controller('BookkeepingCtrl', ['$scope', 'BookkeepingProxy', '$modal', '$filter',
    function($scope, BookkeepingProxy, $modal, $filter) {
        $scope.currentVerPage = 1;
        $scope.selectedVerAccount = {};

        $scope.verification = {posts: 
                    [ {debit: 0, credit: 0},
                    {debit: 0, credit: 0}],
                    transactionDate: $filter('date')(new Date(),'yyyy-MM-dd')
            };
        
        $scope.sumDebit = function(){
            return sumDebit();
        };
        
        $scope.sumCredit = function() {
            return sumCredit();
        };
        
        function sumDebit() {
            var total = 0;
            for(var i = 0; i < $scope.verification.posts.length; i++){
                var debit = $scope.verification.posts[i].debit;
                total += debit;
            }
            return total;
        }
        
        function sumCredit() {
            var total = 0;
            for(var i = 0; i < $scope.verification.posts.length; i++){
                var credit = $scope.verification.posts[i].credit;
                total += credit;
            }
            return total;
        }
        
        $scope.addRow = function() {
            $scope.verification.posts[$scope.verification.posts.length] = {debit: 0, credit: 0};
        };
        
        $scope.removeRow = function(post) {
            var index = $scope.verification.posts.indexOf(post);
            $scope.verification.posts.splice(index, 1);
            $scope.accountls.splice(index, 1);
        };
        
        $scope.create = function() {
            BookkeepingProxy.createManBook($scope.verification)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.verification = {posts: 
                                        [ {debit: 0, credit: 0},
                                        {debit: 0, credit: 0}],
                                        transactionDate: $filter('date')(new Date(),'yyyy-MM-dd'),
                                        description: ""
                                };
                            $scope.accountls = [];
                        }
                    }).error(function() {
                        console.log("mbk:create: error");
                    });
        };
        
        $scope.accountls = [];
        $scope.open = function (index) {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/accountSelecterModal.html',
                controller: 'ModalInstanceAccountCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedAccount) {
                if(!angular.isDefined(index)) {
                    $scope.showaccount = selectedAccount.number + ' - ' + selectedAccount.name;
                    $scope.selectedVerAccount = selectedAccount;
                    getVerifications();
                } else {
                    $scope.verification.posts[index].accountid = selectedAccount.number;
                    $scope.accountls[index] = selectedAccount.number + ' - ' + selectedAccount.name;
                }
            });
        };
        
        function getVerifications() {
            var currentPage = $scope.currentVerPage - 1;
            var tempaccount = {number: $scope.selectedVerAccount.number, startrange: currentPage};
            BookkeepingProxy.getVerificationsByAccount(tempaccount)
                    .success(function(verifications) {
                        $scope.verifications = verifications;
                    }).error(function() {
                        console.log('error: getVerificationsByAccount');
                    });
            BookkeepingProxy.countVerificationsByAccount(tempaccount)
                    .success(function(size) {
                        $scope.countverifications = size;
                    }).error(function() {
                        console.log('error: countVerificationsByAccount');
                    });
        }
        
        $scope.verPageChanged = function() {
            getVerifications();
        };
                
        $scope.opencal = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            
            $scope.opened = true;
        };
        
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            yearRange: 1,
            maxMode: 'month',
            currentText: 'Idag'
        };            
    }
]);

bookkeepingControllers.controller('ModalInstanceAccountCtrl', 
    function ($scope, $modalInstance, BookkeepingProxy) {

    $scope.radioModel = 0;
    $scope.currentPage = 1;
    
    $scope.selected = function(account) {
        account: account;
    };

    $scope.ok = function () {
      $modalInstance.close($scope.selected.account);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
    
    $scope.search = function() {
        $scope.currentPage = 1;
        search();
    };
    
    $scope.autosearch = function() {
        $scope.currentPage = 1;
        if($scope.radioModel === 0 && $scope.account.number > 9 || 
                $scope.radioModel === 1 && $scope.account.name.length > 2) {
            search();
        } else {
            $scope.accounts = {};
            $scope.maxsize = 0;
        }
    };
    
    function search() {
        var account;
        var currentPage = $scope.currentPage - 1;
        if($scope.radioModel === 0 && $scope.account.number) {
            account = {number: $scope.account.number, startrange: currentPage};
        } else if($scope.radioModel === 1 && $scope.account.name) {
            account = {name: $scope.account.name, startrange: currentPage};
        } else {
            return;
        }
        BookkeepingProxy.searchAccount(account)
                    .success(function(accounts) {
                        $scope.accounts = accounts;
                    }).error(function() {
                        console.log("mbk:searchAccName: error");
                    });
        BookkeepingProxy.countSearchAccount(account)
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("mbk:countSearchAccName: error");
                    });
    }
    
    $scope.pageChanged = function() {
        search();
    };
    
});

bookkeepingControllers.controller('VerificationCtrl', ['$scope', '$modal', 'BookkeepingProxy',
    function($scope, $modal, BookkeepingProxy) {
        
        $scope.showverinfoboolean = false;
        $scope.verinfo = {};
        $scope.editver = {};
        $scope.currentPage = 1;
        $scope.verifications = getVerifications();
        
        function getVerifications() {
            var pageStart = $scope.currentPage-1 + "";
            // IN SOME WIERD WAY THE SERVER CAN NOT ACCEPT AN INTEGER
            // SO I WILL SEND A STRING
            BookkeepingProxy.getVerifications(pageStart)
                    .success(function(verifications) {
                        $scope.verifications = verifications;
                    }).error(function() {
                console.log("getVer: error");
            });
            BookkeepingProxy.countVerifications()
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("countVerification: error");
                    });
        };
        
        $scope.showverinfo = function(index) {
            if(angular.isDefined(index)) {
                $scope.verinfo = $scope.verifications[index];
            }
            $scope.showverinfoboolean = true;
            $scope.showeditverboolean = false;
            $scope.showverhistoryboolean = false;
            $scope.editver = {};
            // REMOVE FORM IF DEFINED
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
        };
        
        $scope.showverhistory = function() {
            $scope.showverinfoboolean = false;
            $scope.showeditverboolean = false;
            $scope.showverhistoryboolean = true;
            $scope.editver = {};
        };

        $scope.showeditver = function() {
            angular.copy($scope.verinfo, $scope.editver);
            $scope.editver.oldposts = [];
            $scope.accountls = [];
            $scope.showverinfoboolean = false;
            $scope.showeditverboolean = true;
            $scope.showverhistoryboolean = false;
            // REMOVE FORM IF DEFINED
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
        };
        
        $scope.pageChanged = function() {
            getVerifications();
        };
        
        $scope.removePost = function(post) {
            if($scope.editver.oldposts === null) {
                $scope.editver.oldposts = [];
            }
            $scope.editver.oldposts[$scope.editver.oldposts.length] = post;
            post.removed = true;
        };
        
        $scope.restorePost = function(post) {
            $scope.editver.oldposts.splice($scope.editver.oldposts.indexOf(post), 1);
            delete post.removed;
        };
        
        $scope.sumDebit = function(){
            return sumDebit();
        };
        
        $scope.sumCredit = function() {
            return sumCredit();
        };
        
        function sumDebit() {
            var total = 0;
            if($scope.editver.debitposts) {
                for(var i = 0; i < $scope.editver.debitposts.length; i++){
                    if(!$scope.editver.debitposts[i].removed) {
                        total += $scope.editver.debitposts[i].sum;
                    }
                }
            }
            if($scope.editver.posts) {
                for(var i = 0; i < $scope.editver.posts.length; i++){
                    total += $scope.editver.posts[i].debit;
                }
            }
            return total;
        }
        
        function sumCredit() {
            var total = 0;
            if($scope.editver.creditposts) {
                for(var i = 0; i < $scope.editver.creditposts.length; i++){
                    if(!$scope.editver.creditposts[i].removed) {
                        total += $scope.editver.creditposts[i].sum;
                    }
                }
            }
            if($scope.editver.posts) {
                for(var i = 0; i < $scope.editver.posts.length; i++){
                    total += $scope.editver.posts[i].credit;
                }
            }
            return total;
        }
        
        $scope.addNewpost = function() {
            if($scope.editver.posts === null) {
                $scope.editver.posts = [];
            }
            $scope.editver.posts[$scope.editver.posts.length] = {debit: 0, credit: 0};
        };
        
        $scope.removeNewpost = function(post) {
            var index = $scope.editver.posts.indexOf(post);
            $scope.editver.posts.splice(index, 1);
            $scope.accountls.splice(index, 1);
        };
        
        $scope.accountls = [];
        $scope.openaccount = function (index) {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/accountSelecterModal.html',
                controller: 'ModalInstanceAccountCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedAccount) {
                $scope.editver.posts[index].accountid = selectedAccount.number;
                $scope.accountls[index] = selectedAccount.number + ' - ' + selectedAccount.name;
            });
        };
        
        $scope.sendver = {};
        $scope.submit = function() {
            angular.copy($scope.editver, $scope.sendver);
            if(angular.isDefined($scope.sendver.oldposts)) {
                // REMOVE THE REMOVED-VAR
                angular.forEach($scope.sendver.oldposts, function(post) {
                    delete post.removed;
                });
            }
            BookkeepingProxy.editVerification($scope.sendver)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            // REOLAD
                            var index = $scope.verifications.indexOf($scope.verinfo);
                            $scope.verifications = getVerifications();
                            $scope.editver = {};
                            $scope.$watch('verifications', function(ver) {
                                if(angular.isDefined(ver)) {
                                    $scope.verinfo = $scope.verifications[index];
                                }
                             });
                            $scope.showeditverboolean = false;
                            $scope.showverinfoboolean = true;                            
                        }
                    }).error(function() {
                        console.log("editVerification: error");
                    });
        };
    }
]);

bookkeepingControllers.controller('FastbookkeepingCtrl', ['$scope', 'BookkeepingProxy', '$modal', '$filter',
    function($scope, BookkeepingProxy, $modal, $filter) {
        
        $scope.step = 1;
        $scope.steptype = [0,0,0,0];
        $scope.posts = [];
        $scope.sum = 0;
        $scope.moms = 0;
        
        $scope.calcStep2 = function() {
            var moms = $scope.sum * .2;
            var type;
            if($scope.steptype[0] === 1)
                type = "credit";
            else 
                type = "debit";
            addPost(2611, "Utgående moms på försäljning inom Sverige, 25%", type, moms);
            addPost(3000, "Försäljning inom Sverige", $scope.steptype[1], $scope.sum - moms);
        };
        
        $scope.calcStep3 = function() {
            var type;
            if($scope.steptype[0] === 1)
                type = "debit";
            else 
                type = "credit";
            if($scope.steptype[2] === 1) {
                addPost(1910, "Kassa", type, $scope.sum);
            } else {
                addPost(1920, "PlusGiro", type, $scope.sum);
            }
        };
        
        
        function addPost(acc_number, acc_name, type, sum) {
            var debit;
            var credit;
            if(type === "debit") {
                debit = sum;
                credit = 0;
            } else {
                debit = 0;
                credit = sum;
            }
            var temp_post = {accountid: acc_number, accountname: acc_name,
                            debit: debit, credit: credit};
            $scope.posts = $scope.posts.concat([temp_post]);
        };
    }
]);