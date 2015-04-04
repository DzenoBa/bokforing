
/**
 * BOK CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var bookkeepingControllers = angular.module('BookkeepingControllers', ['ui.bootstrap']);

bookkeepingControllers.controller('BookkeepingCtrl', ['$scope', 'BookkeepingProxy', '$modal', '$filter',
    function($scope, BookkeepingProxy, $modal, $filter) {
        $scope.rows = 2;
        $scope.currentVerPage = 1;
        $scope.selectedVerAccount = {};
        
        $scope.getNumber = function(num) {
            return new Array(num);   
        };

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
            for(var i = 0; i < $scope.rows; i++){
                var debit = $scope.verification.posts[i].debit;
                total += debit;
            }
            return total;
        }
        
        function sumCredit() {
            var total = 0;
            for(var i = 0; i < $scope.rows; i++){
                var credit = $scope.verification.posts[i].credit;
                total += credit;
            }
            return total;
        }
        
        $scope.addRow = function() {
            $scope.verification.posts[$scope.rows] = {debit: 0, credit: 0};
            $scope.rows = $scope.rows+1;
        };
        
        $scope.removeRow = function(index) {
            $scope.verification.posts.splice(index, 1);
            $scope.accountls.splice(index, 1);
            $scope.rows = $scope.rows-1;
        };
        
        $scope.create = function() {
            BookkeepingProxy.createManBook($scope.verification)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.verification = {posts: 
                                        [ {debit: 0, credit: 0},
                                        {debit: 0, credit: 0}],
                                        transactionDate: $filter('date')(new Date(),'yyyy-MM-dd')
                                };
                            $scope.rows = 2;
                            $scope.accountls = [];
                        }
                    }).error(function() {
                        console.log("mbk:create: error");
                    });
        };
        
        $scope.accountls = [];
        $scope.open = function (index) {

            var modalInstance = $modal.open({
                templateUrl: 'myModalContent.html',
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

bookkeepingControllers.controller('VerificationCtrl', ['$scope', 'BookkeepingProxy',
    function($scope, BookkeepingProxy) {
        
        $scope.showverinfoboolean = false;
        $scope.verinfo = {};
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
            $scope.verinfo = $scope.verifications[index];
            $scope.showverinfoboolean = true;
        };
        
        $scope.pageChanged = function() {
            getVerifications();
        };
    }
]);