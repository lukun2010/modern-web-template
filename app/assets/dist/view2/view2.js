/**
 * Created by lu.kun on 2015/4/8.
 */
'use strict';

angular.module('myApp.view2', ['ngRoute', 'ngWebsocket'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/view2', {
            templateUrl: "/assets/dist/view2/view2.html",
            controller: 'View2Ctrl'
        });
    }])
    .controller('View2Ctrl', function($scope, $log, WSService, $modal) {

        var columnDefs = [
            {displayName: "版本", field: "version"},
            {displayName: "主题", field: "service"},
            {displayName: "服务器", field: "server"},
            {displayName: "时间戳", field: "timestamp"}
        ];

        $scope.sendMessage = function(message) {
            WSService.send("data" + message);
        };

        $scope.rowData = FixedQueue(10);

        WSService.receive().then(null, null, function(message) {
            var jsonObject = JSON.parse(message);
            $scope.columnDefs = jsonObject["headerlabel"];
            var headerData = jsonObject["headerdata"][0];
            headerData["datalabel"] = jsonObject["datalabel"];
            headerData["data"] = jsonObject["data"];
            //$log.log(headerData);
            $scope.rowData.push(headerData);
            $scope.gridOptions.rowData = $scope.rowData;
            $scope.gridOptions.api.onNewCols();
            $scope.gridOptions.api.onNewRows();
        });

        $scope.gridOptions = {
            columnDefs: columnDefs,
            rowData: $scope.headerData,
            dontUseScrolls: true, // because so little data, no need to use scroll bars
            rowSelection: 'single',
            rowSelected: rowSelectedFunc
        };

        $scope.rowSelectedData = {};

        function rowSelectedFunc(row) {
            $scope.rowSelectedData = {
                "datalabel" : row["datalabel"],
                "data" : row["data"]
            }
            $scope.showDataModal();
        }

        $scope.showDataModal = function (size) {

            $log.log("弹出查看数据对话框");
            var modalInstance = $modal.open({
                templateUrl: 'showDataModal.html',
                controller: 'showDataModalCtrl',
                size: size,
                resolve: {
                    rowSelectedData: function () {
                        return $scope.rowSelectedData;
                    }
                }
            });

            modalInstance.result.then(function () {
                $log.info("返回");
            }, function () {
                $log.info("返回");
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

    })
    .controller('showDataModalCtrl', function ($scope, $modalInstance, rowSelectedData, $log) {

        $scope.rowSelectedData = rowSelectedData;

        $log.log($scope.rowSelectedData["datalabel"]);
        $log.log($scope.rowSelectedData["data"]);

        $scope.gridOptionsShowData = {
            columnDefs: $scope.rowSelectedData["datalabel"],
            rowData: $scope.rowSelectedData["data"]
        };

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancelDeleteColumn');
        };
    })
    .service("WSService", function($websocket, $q) {
        var ws;
        var service = {};
        var listener = $q.defer();

        service.send = function(message) {
            ws.$emit("topic", message);
        };

        service.receive = function() {
            return listener.promise;
        };

        service.disconnect = function() {
            ws.$close();
        };

        var initialize = function() {
            ws = $websocket.$new({
                url: 'ws://localhost:9000/message2',
                reconnect: true,
                reconnectInterval: 5000
            });
            ws.$on('message', function(data) {
                listener.notify(data);
            });
        };

        initialize();

        return service;

    });