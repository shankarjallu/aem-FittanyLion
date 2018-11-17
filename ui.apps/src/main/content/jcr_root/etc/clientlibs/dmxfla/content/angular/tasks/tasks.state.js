(function(){
    
    angular.module('fittanyUiApp')
        .config(['$stateProvider', function($stateProvider) {
            console.log("initializing tasks component ...");
                $stateProvider
                    .state('tasks', {
                        url: '/tasks',
                        templateUrl: '/apps/hha/dmxfla/components/content/tasks/task-template.html',
                        controller: 'TasksController'
    
                    });
    
        }]);
    })();
