(function() {
    
    function TasksController ($scope, User, $timeout,$rootScope) {
        // Get 

    	$scope.user = User.getUser();
        $scope.tasks = $scope.user.tasks; /// user and tasks will be on same response obj when user logins
        $scope.totalTasksCount = $scope.user.taskTotalChancesCount;
        // listen for updated tasklist
        $rootScope.$on("tasklistUpdated", function(data){
            if(data){
                $scope.tasks = User.getTasks(); 
            }
        })

        
        //$scope.taskDone = false;
        $scope.updateTask = function(task){
            //var update = this;
            $scope.success = false;
            $scope.error = false;
            console.log("update the task done.");
            var promise = User.updateTask(task);
                promise.then(function(res){
                    if(res.status == 200){
                        $scope.success = true;
                        $scope.error = false;
                    }else {
                        $scope.error = true;
                        $timeout(function(){
                            $scope.success = false;
                            $scope.error = false;
                        },30000); // remove error message from the dom after 30 secs
                    }
                }, function(err){
                    $scope.success = false;
                    $scope.error = true;
                    $timeout(function(){
                            $scope.success = false;
                            $scope.error = false;
                    },30000); // remove error message from the dom after 30 secs
                });
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
            var startDate = new Date($scope.user.taskStartDate); 
            var timeDiff = Math.abs(currentTime.getTime() - startDate.getTime());
            var diffWeeks = Math.ceil(timeDiff / (1000 * 3600 * 24 * 7));
            return diffWeeks; 
        }

    };
    
    angular.module('fittanyUiApp')
        .controller('TasksController', TasksController);


})();