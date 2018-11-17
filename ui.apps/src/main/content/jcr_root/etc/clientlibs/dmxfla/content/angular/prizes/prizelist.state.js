(function(){

    angular.module('fittanyUiApp')
        .config(['$stateProvider', function($stateProvider) {
            console.log("initializing rewards component ...");
                $stateProvider
                    .state('rewards', {
                        url: '/rewards',
                        templateUrl: '/apps/hha/dmxfla/components/content/prizelist/prizelist-template.html',
                        controller: 'RewardsController'
                    });
    
        }]);
    })();
