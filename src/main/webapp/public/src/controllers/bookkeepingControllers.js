
/**
 * BOK CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var bookkeepingControllers = angular.module('BookkeepingControllers', ['ui.bootstrap']);

bookkeepingControllers.controller('ManBKCtrl', ['$scope', 'BookkeepingProxy', '$modal',
    function($scope, BookkeepingProxy, $modal) {
        $scope.rows = 2;
        $scope.getNumber = function(num) {
            return new Array(num);   
        };

        $scope.verification = {posts: 
                    [ {debit: 0, credit: 0},
                    {debit: 0, credit: 0}]
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
            $scope.verification.posts[index].debit = null;
            $scope.verification.posts[index].credit = null;
            $scope.rows = $scope.rows-1;
        };
        
        $scope.create = function() {
            BookkeepingProxy.createManBook($scope.verification)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.verification = {posts: 
                                        [ {debit: 0, credit: 0},
                                        {debit: 0, credit: 0}]
                                };
                        }
                    }).error(function() {
                        console.log("mbk:create: error");
                    });
        };

        $scope.open = function (index) {

            var modalInstance = $modal.open({
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceAccountCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.verification.posts[index].account = selectedItem;
            });
        };
            
    }
]);

bookkeepingControllers.controller('ModalInstanceAccountCtrl', 
    function ($scope, $modalInstance, BookkeepingProxy) {

    $scope.radioModel = 0;
    
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
        search();
    };
    
    $scope.autosearch = function() {
        if($scope.radioModel === 0 && $scope.account.number > 9 || 
                $scope.radioModel === 1 && $scope.account.name.length > 2) {
            search();
        }
    };
    
    function search() {
        if($scope.radioModel === 0 && $scope.account.number) {
            var account = {number: $scope.account.number};
            BookkeepingProxy.searchAccount(account)
                    .success(function(accounts) {
                        $scope.accounts = accounts;
                    }).error(function() {
                        console.log("mbk:searchAccNumber: error");
                    });
        } else if($scope.radioModel === 1 && $scope.account.name) {
            var account = {name: $scope.account.name};
            BookkeepingProxy.searchAccount(account)
                    .success(function(accounts) {
                        $scope.accounts = accounts;
                    }).error(function() {
                        console.log("mbk:searchAccName: error");
                    });
        }
    }
});