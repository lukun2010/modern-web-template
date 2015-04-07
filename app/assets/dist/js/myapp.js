/**
 * Created by lu.kun on 2015/4/5.
 */

angular.module('myApp', ['ngRoute', 'ui.bootstrap', 'angularGrid', 'myApp.view1'])
    .config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            otherwise({
                redirectTo: '#'
            });
    }]);

