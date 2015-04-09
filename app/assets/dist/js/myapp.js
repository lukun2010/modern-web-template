/**
 * Created by lu.kun on 2015/4/5.
 */

angular.module('myApp', ['ngRoute', 'ui.bootstrap', 'myApp.view1', 'myApp.view2'])
    .config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            otherwise({
                redirectTo: '#'
            });
    }]);

