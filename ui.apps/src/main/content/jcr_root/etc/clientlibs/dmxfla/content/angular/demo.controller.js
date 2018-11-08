(function() {


    var app = angular.module('app', ['ui.router']);

    app.controller('MainController',['$scope', function($scope) {
        $scope.name = "Volvo";
    }]);


})();

(function(){

angular.module('app')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing admin component ...");
            $stateProvider
                .state('home', {
                    url: '/home',
                    templateUrl: '/apps/hha/dmxfla/components/content/angular/template.html'
                });

    }]);
})();
