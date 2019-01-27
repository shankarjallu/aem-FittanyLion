(function() {

    function TasksController($scope, User, $timeout, $rootScope) {
        // Get 
         $scope.user = User.getUser();
         $scope.tasks = $scope.user.tasks; 
       // $scope.user = User.getUser()[0];
        //$scope.tasks = User.getTasks(); /// user and tasks will be on same response obj when user logins
        //$scope.totalTasksCount = $scope.user.taskTotalChancesCount;
        $scope.congratsCard = false;
        // listen for updated tasklist
        // $rootScope.$on("tasklistUpdated", function(data) {
        //     if (data) {
        //         $scope.tasks = User.getTasks();
        //     }
        // })


        //$scope.taskDone = false;
        $scope.updateTask = function(task,evt) {
            //var update = this;
            var el = evt.currentTarget;
            $scope.success = false;
            $scope.error = false;
            console.log("update the task done.");
            var promise = User.updateTask(task);
            promise.then(function(res) {
                if (res.data.statusCode == 200) {
                    $scope.success = true;
                    $scope.error = false;
                    el.disabled = true;
                    var icon = document.getElementById("toggleIcon"+task.taskID);
                    icon.style.display = "none";
                    var taskCompleteContainer = document.getElementById("taskComplete"+task.taskID);
                    var taskDetailContent = document.getElementById(task.taskID);
                    taskCompleteContainer.style.display = "block";
                    taskDetailContent.style.display = "none";
                    taskDetailContent.classList.remove("show");
                    $scope.congratsCard = res.data.congratsCard;
                } else {
                    $scope.error = true;
                    el.checked = false;
                    var taskCompleteContainer = document.getElementById("taskComplete"+task.taskID);
                    taskCompleteContainer.style.display = "none";
                    $timeout(function() {
                        $scope.success = false;
                        $scope.error = false;
                    }, 30000); // remove error message from the dom after 30 secs
                }
            }, function(err) {
                $scope.success = false;
                $scope.error = true;
                el.checked = false;
                 var taskCompleteContainer = document.getElementById("taskComplete"+task.taskID);
                taskCompleteContainer.style.display = "none";
                $timeout(function() {
                    $scope.success = false;
                    $scope.error = false;
                }, 30000); // remove error message from the dom after 30 secs
            });
        }

        $scope.manualTasks = [{
            "taskTitle": "Fittness Challenge",
            "taskDescription": "Sorry...We Have No Games For this Week",
            "taskUserManual": "Please Visit Again",
            "TaskCompleteIndicator": "Y",
            "taskID": 12345,
            "taskSequence": 1,
            "id": 1
        }, {
            "taskTitle": "Nutrition Challenge",
            "taskDescription": "Sorry...We Have No Games For this We",
            "taskUserManual": " Please Visit Again",
            "TaskCompleteIndicator": "Y",
            "taskID": 12346,
            "taskSequence": 2,
            "id": 2
        }, {
            "taskTitle": "Wellness Challenge",
            "taskDescription": "Sorry...We Have No Games For this We",
            "taskUserManual": "Please Visit Again ",
            "TaskCompleteIndicator": "Y",
            "taskID": 12347,
            "taskSequence": 3,
            "id": 3
        }];

        $scope.totalSelected = function() {
            if ($scope.user.tasks == undefined) {
                return 0;
            }
            var doneArray = $scope.user.tasks.filter(function(task) {
                return task.taskDone == true;
            });
            return doneArray.length;
        }

        $scope.totalTaskType = function(taskType) {
            if ($scope.user.tasks == undefined) {
                return 0;
            }
            var doneArray = $scope.user.tasks.filter(function(task) {
                return task.taskDone == true && task.taskType == taskType;
            });
            return doneArray.length;
        }


        // This code is for Days left in week // 
        $scope.daysLeftFn = function() {
             var daysLeft = 0;
            var currentTime = new Date();
            var dayOfWeek = currentTime.getDay();
            if(dayOfWeek == 0){
               daysLeft = 1;
            }else{
                 daysLeft = 7 - (dayOfWeek-1);
            }

            return daysLeft;
        }

        $scope.currentWeekFn = function() {
            var currentTime = new Date();
            var startDate = $scope.user.taskStartDate;
            var splitDate = startDate.split("-");
            var date = splitDate[0];
            var month = splitDate[1];
            var year = splitDate[2];
           // var formatDate = startDate.replace(/-/g, "/");
            var startDateFormatted = new Date(month+"/"+date+"/"+year);
            var timeDiff = Math.abs(currentTime.getTime() - startDateFormatted.getTime());
            var diffWeeks = Math.ceil(timeDiff / (1000 * 3600 * 24 * 7));
            return diffWeeks;
        }

    };

    TasksController.$inject = ['$scope','User','$timeout','$rootScope'];

    angular.module('fittanyUiApp')
        .controller('TasksController', TasksController);


})();