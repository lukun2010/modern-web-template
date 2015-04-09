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
    .controller('View2Ctrl', function($scope, $log, WSService) {

        var columnDefs = [
            {displayName: "消息", field: "message"}
        ];

        $scope.sendMessage = function(message) {
            WSService.send("data" + message);
        };

        $scope.rowData = FixedQueue(10);

        WSService.receive().then(null, null, function(message) {
            $scope.rowData.push({
                "message" : message
            });
            $scope.gridOptions.rowData = $scope.rowData;
            $scope.gridOptions.api.onNewRows()
        });

        $scope.gridOptions = {
            columnDefs: columnDefs,
            rowData: $scope.rowData,
            dontUseScrolls: true // because so little data, no need to use scroll bars
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