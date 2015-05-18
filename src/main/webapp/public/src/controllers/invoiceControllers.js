
/**
 * INVOICE CONTROLLERS
 * 
 * @author Dženan
 */

'use strict';

var invoiceControllers = angular.module('InvoiceControllers', ['ui.bootstrap']);

invoiceControllers.controller('InvoiceCtrl', ['$scope', 'InvoiceProxy', '$modal',
    function($scope, InvoiceProxy, $modal) {
        
        $scope.currentPage = 1;
        $scope.invoice = {productls: [{amount: 0}], ftax:false};
        $scope.createboolean = true;
        $scope.viewboolean = false;
        
        $scope.create = function() {
            InvoiceProxy.create($scope.invoice)
                    .success(function(form) {
                        $scope.form = form;
                        if(form.numErrors === 0) {
                            $scope.invoice = {productls: [{amount: 0}], ftax:false};
                            getInvoices();
                            countInvoices();
                        }
                    }).error(function() {
                        console.log("invoice:create: error");
                    });
        };
        
        $scope.addProduct = function() {
            $scope.invoice.productls[$scope.invoice.productls.length] = {amount: 0};
        };
        
        $scope.removeProduct = function(product) {
            var index = $scope.invoice.productls.indexOf(product);
            $scope.invoice.productls.splice(index, 1);
        };
        
        $scope.openproducts = function (index) {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/productSelecterModal.html',
                controller: 'ModalInstanceProductCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedProduct) {
                $scope.invoice.productls[index].id = selectedProduct.id;
                $scope.invoice.productls[index].name = selectedProduct.name;
            });
        };
        
        $scope.opencustomers = function () {

            var modalInstance = $modal.open({
                templateUrl: 'private/modals/customerSelecterModal.html',
                controller: 'ModalInstanceCustomerCtrl',
                size: 'lg'
            });

            modalInstance.result.then(function (selectedCustomer) {
                $scope.invoice.customer = {customernumber: selectedCustomer.customernumber, name: selectedCustomer.name};
            });
        };
        
        function getInvoices() {
            var stringIndex = $scope.currentPage-1 + "";
            var invoice = {startrange: stringIndex};
            if(angular.isDefined($scope.searchcustomer)) {
                invoice.name = $scope.searchcustomer;
            }
            InvoiceProxy.getInvoices(invoice)
                    .success(function(invoices) {
                        $scope.invoices = invoices;
                    }).error(function() {
                        console.log("product:getInvoices: error");
                    });
        };
        
        function countInvoices() {
            var invoice = {};
            if(angular.isDefined($scope.searchcustomer)) {
                invoice.name = $scope.searchcustomer;
            }
            InvoiceProxy.countInvoices(invoice)
                    .success(function(size) {
                        $scope.maxsize = size;
                    }).error(function() {
                        console.log("product:countInvoices: error");
                    });
        };
        
        $scope.showview = function(invoice) {
            $scope.createboolean = false;
            $scope.viewboolean = true;
            $scope.invoice = angular.copy(invoice);
        };
        
        $scope.showcreate = function() {
            if(angular.isDefined($scope.form)) {
                delete $scope.form;
            }
            $scope.viewboolean = false;
            $scope.createboolean = true;
            $scope.invoice = {productls: [{amount: 0}], ftax:false};
        };
        
        // INIT
        getInvoices();
        countInvoices();
    }
]);