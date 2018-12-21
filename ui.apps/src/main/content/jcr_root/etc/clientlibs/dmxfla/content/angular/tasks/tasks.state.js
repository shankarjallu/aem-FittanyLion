(function(){
    
    angular.module('fittanyUiApp')
        .config(['$stateProvider', function($stateProvider) {
            console.log("initializing tasks component ...");
                $stateProvider
                    .state('tasks', {
                        url: '/tasks',
     templateUrl: '/content/digitalmarketing/en/highmark/fittanylion/tasks/jcr:content/section-content.html',
                        controller: 'TasksController'
    
                    });
    
        }]);
    })();
