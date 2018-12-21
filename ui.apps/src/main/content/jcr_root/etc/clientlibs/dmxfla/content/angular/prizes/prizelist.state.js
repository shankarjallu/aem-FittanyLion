(function(){

    angular.module('fittanyUiApp')
        .config(['$stateProvider', function($stateProvider) {
            console.log("initializing rewards component ...");
                $stateProvider
                    .state('rewards', {
                        url: '/rewards',
        templateUrl: '/content/digital-marketing/en/highmark/fittanylion/tasks/jcr:content/sectioncontent.html',
                        controller: 'RewardsController'
                    });
    
        }]);
    })();
