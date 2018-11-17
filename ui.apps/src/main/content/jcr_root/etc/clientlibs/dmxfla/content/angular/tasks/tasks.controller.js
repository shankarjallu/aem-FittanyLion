(function() {
    
    function TasksController ($scope, User) {
        // Get 

        $scope.user = User.get({id: 1}); 

        $scope.updateUser = function(){
            $scope.user.$update();
        }

        $scope.taskTypes = ["FITNESS Challenge", "WELL-BEING Challenge", "NUTRITION Challenge"]

        $scope.totalSelected = function() {
            if ($scope.user.tasks == undefined) {
                return 0;
            }
            var doneArray = $scope.user.tasks.filter(function(task){
                return task.taskDone == true; 
            });
            return doneArray.length;
        }

        $scope.totalTaskType = function(taskType) {
            if ($scope.user.tasks == undefined) {
                return 0;
            }
            var doneArray = $scope.user.tasks.filter(function(task){
                return task.taskDone == true && task.taskType == taskType; 
            });   
            return doneArray.length;
        }


        // This code is for Days left in week // 
        $scope.daysLeftFn = function() {
            var currentTime = new Date();
            var dayOfWeek = currentTime.getDay();
            var daysLeft = 7 - dayOfWeek; 
            return daysLeft; 
        }

        $scope.currentWeekFn = function() {
            var currentTime = new Date();
            var startDate = new Date("09/30/2018"); 
            var timeDiff = Math.abs(currentTime.getTime() - startDate.getTime());
            var diffWeeks = Math.ceil(timeDiff / (1000 * 3600 * 24 * 7));
            return diffWeeks; 
        }

    };

    TasksController.$inject = ['$scope', 'User'];
    angular.module('fittanyUiApp')
        .controller('TasksController', TasksController);


})();
