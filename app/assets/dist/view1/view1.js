'use strict';

angular.module('myApp.view1', ['ngRoute',  'angularGrid'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: "/assets/dist/view1/view1.html",
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', function($scope, $http, $log, $modal) {

      console.log("View1Ctrl");

      $scope.schemaName = "请选择主题";
      $scope.versionName = "请选择版本";

      $scope.schemaItems = [];
      $scope.versionItems = [];

        $scope.colSelected = "";

        $scope.validSchemaNameSelected = false;
        $scope.validVersionNameSelected = false;

        $scope.validSchemaName = function() {
            if ($scope.schemaName == "请选择主题") {
                $scope.validSchemaNameSelected = false;
            } else {
                $scope.validSchemaNameSelected = true;
            }
        }

        $scope.validVersionName = function() {
            if ($scope.versionName == "请选择版本") {
                $scope.validVersionNameSelected = false;
            } else {
                $scope.validVersionNameSelected = true;
            }
        }

      $scope.status = {
        isopen: false
      };

      $scope.toggleDropdown = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.status.isopen = !$scope.status.isopen;
      };

      $scope.getSchemaNames = function(open) {
        $log.log('Dropdown is now: ', open);
        if (open) {
          $log.log('getSchemaNames');
          var futureResponse = $http.get("/schemas");
          futureResponse.success(function (data, status, headers, config) {
            $scope.schemaItems = data;
          });
          futureResponse.error(function (data, status, headers, config) {
            throw new Error('Something went wrong...');
          });
        }
      }

      $scope.getVersionsBySchemaName = function(open) {
        if (open) {
          var futureResponse = $http.get("/schemas/" + $scope.schemaName + "/versions");
          futureResponse.success(function (data, status, headers, config) {
            $scope.versionItems = data;
          });
          futureResponse.error(function (data, status, headers, config) {
            throw new Error('Something went wrong...');
          });
        }
      }

      $scope.setSchemaSelected = function(item) {
        $scope.schemaName = item;
          $scope.versionName = "请选择版本";
          $scope.validSchemaName();
          $scope.validVersionName();
      }

      $scope.setVersionSelected = function(item) {
        $scope.versionName = item;
          $scope.validVersionName();
      }

        //Array Remove - By John Resig (MIT Licensed)
        Array.remove = function(array, from, to) {
            var rest = array.slice((to || from) + 1 || array.length);
            array.length = from < 0 ? array.length + from : from;
            return array.push.apply(array, rest);
        };

        $scope.fieldCnt = -1;

        $scope.getSchema = function() {
            $log.log("getSchema");
            var futureResponse = $http.get("/schemas/" + $scope.schemaName + "/versions/" + $scope.versionName);
            futureResponse.success(function (data, status, headers, config) {
                $log.log("getSchema Success");
                $log.log(data);
                $scope.rowData = data;
                $scope.fieldCnt = $scope.rowData.length;
                $scope.gridOptions.rowData = $scope.rowData;
                $scope.gridOptions.api.onNewRows()
            });
            futureResponse.error(function (data, status, headers, config) {
                throw new Error('Something went wrong...');
            });
        }

        var columnDefs = [
            {displayName: "字段名称", field: "name"},
            {displayName: "字段类型", field: "type"}
        ];

        $scope.rowData = [];

        $scope.gridOptions = {
            columnDefs: columnDefs,
            rowData: $scope.rowData,
            dontUseScrolls: true, // because so little data, no need to use scroll bars
            rowSelection: 'single',
            rowSelected: rowSelectedFunc
        };

        function rowSelectedFunc(row) {
            $log.log("row " + row.name + " selected");
            $scope.colSelected = row.name;
        };

        $scope.newColumnModal = function (size) {

            $log.log("弹出新增字段对话框");
            var modalInstance = $modal.open({
                templateUrl: 'newColumnModal.html',
                controller: 'newColumnModalCtrl',
                size: size
            });

            modalInstance.result.then(function (result) {
                $log.info("确认新增字段");
                $log.info("rowData添加前的长度是：" + $scope.rowData.length);
                $scope.rowData.push(result);
                $log.info("rowData添加后的长度是：" + $scope.rowData.length);
                $scope.gridOptions.rowData = $scope.rowData;
                $scope.gridOptions.api.onNewRows();
                // 显示添加成功
                $scope.alerts.push({
                    "type" : "success",
                    "msg" : "字段添加成功！"
                });
            }, function () {
                $log.info("取消新增字段");
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.confirmDeleteModal = function (size) {

            $log.log("弹出删除确认对话框");
            var modalInstance = $modal.open({
                templateUrl: 'confirmDeleteModal.html',
                controller: 'confirmDeleteModalCtrl',
                size: size,
                resolve: {
                    colSelected: function () {
                        return $scope.colSelected;
                    }
                }
            });

            modalInstance.result.then(function () {
                $log.info("确定删除");
                $log.log("rowData长度：" + $scope.rowData.length);
                for(var i = 0; i < $scope.rowData.length; i++){
                    if ($scope.rowData[i].name == $scope.colSelected) {
                        $log.log("找到了，可以删除");
                        // 判断是否是原有字段
                        if (i < $scope.fieldCnt) {
                            $scope.alerts.push({
                                "type" : "danger",
                                "msg" : "不可删除原有字段！"
                            });
                            break;
                        }
                        // 开始删除
                        Array.remove($scope.rowData, i);
                        $scope.gridOptions.rowData = $scope.rowData;
                        $scope.gridOptions.api.onNewRows();
                        $log.log("删除完成");
                        break;
                    }
                }
            }, function () {
                $log.info("取消删除");
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.alerts = [];

        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.submitSchemaModal = function (size) {

            $log.log("弹出提交确认对话框");
            var modalInstance = $modal.open({
                templateUrl: 'submitSchemaModal.html',
                controller: 'submitSchemaModalCtrl',
                size: size,
                resolve: {
                    schemaName: function () {
                        return $scope.schemaName;
                    }
                }
            });

            modalInstance.result.then(function () {
                $log.info("确定提交");
                $log.info("开始提交");
                var futureResponse = $http.post("/schemas/" + $scope.schemaName, $scope.rowData);
                futureResponse.success(function (data, status, headers, config) {
                    $log.log("submit Schema Success");
                    $log.log(data);
                    $scope.alerts.push(data);
                });
                futureResponse.error(function (data, status, headers, config) {
                    $scope.alerts.push({
                        "type" : "danger",
                        "msg" : "提交失败！"
                    });
                });
            }, function () {
                $log.info("取消提交");
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

    })
    .controller('confirmDeleteModalCtrl', function ($scope, $modalInstance, colSelected) {

        $scope.colSelected = colSelected;

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancelDeleteColumn');
        };
    })
    .controller('newColumnModalCtrl', function ($scope, $modalInstance) {

        $scope.colName = "";
        $scope.colType = "";

        $scope.ok = function () {
            console.log("colName=" + $scope.colName + ", colType=" + $scope.colType);
            $modalInstance.close({
                "name": $scope.colName,
                "type": $scope.colType
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancelNewColumn');
        };
    })
    .controller('submitSchemaModalCtrl', function ($scope, $modalInstance, schemaName) {

        $scope.schemaName = schemaName;

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancelSubmitSchema');
        };
    });