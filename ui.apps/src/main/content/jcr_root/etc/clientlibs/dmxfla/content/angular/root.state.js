(function(){
     function RootController($scope,$http,$state) {
                console.log("root controller");

            };

RootController.$inject = ['$scope', '$http', '$state'];

angular.module('fittanyUiApp')
	.controller('RootController', RootController)
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing root component ...");
            $stateProvider
                .state('root', {
                    url: '/',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/FittanyLandingPage.html?wcmmode=disabled',
                    controller: 'RootController'

                });

    }]);
})();

