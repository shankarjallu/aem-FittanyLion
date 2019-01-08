(function() {

    var taskChange = function(AuthService,User) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope: {
                taskChange: '=taskChange'
            },
            link: function(scope, elem, attrs, ctrl) {
                scope.$watch('taskChange', function(nv, ov) {
                    if (nv !== ov) {
                         AuthService.Task(function(response){ // this part needs to be fixed, don't need to call task, task will be in user response
                            if(response.status == 200){
                                User.updateTasksList(response.data); // once user clicks the button, update the tasklists                      
                            }else{
                                $scope.error = true;
                                $scope.message = response.statusText; // to display error message in html
                            }
                        });
                    }
                });
            }
        };
    };

    angular
        .module('fittanyUiApp')
        .directive('taskChange', taskChange);
}());