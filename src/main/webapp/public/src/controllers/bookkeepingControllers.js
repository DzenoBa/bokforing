
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
        
        $scope.items = ['item1', 'item2', 'item3'];

        $scope.open = function (index) {

            var modalInstance = $modal.open({
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceAccountCtrl',
                size: 'lg',
                resolve: {
                    items: function () {
                      return $scope.items;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.verification.posts[index].account = selectedItem;
            });
        };
            
    }
]);

bookkeepingControllers.controller('ModalInstanceAccountCtrl', function ($scope, $modalInstance, items) {

    $scope.items = items;
    $scope.selected = function(item) {
        item: item;
    };

    $scope.ok = function () {
      $modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
});