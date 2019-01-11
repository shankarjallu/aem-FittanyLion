(function() {

    function TasksController($scope, User, $timeout, $rootScope) {
        // Get 
        $scope.user = User.getUser();
        $scope.tasks = $scope.user.tasks; 
        // $scope.user = User.getUser()[0];
        // $scope.tasks = User.getTasks(); /// user and tasks will be on same response obj when user logins
        //$scope.totalTasksCount = $scope.user.taskTotalChancesCount;
        $scope.congratsCard = false;


        //$scope.taskDone = false;
        $scope.updateTask = function(task,evt) {
            //var update = this;
            var el = evt.currentTarget;
            $scope.success = false;
            $scope.error = false;
            console.log("update the task done.");
            var promise = User.updateTask(task);
            promise.then(function(res) {
                if (res.status == 200) {
                    $scope.success = true;
                    $scope.error = false;
                    el.disabled = true;
                    var icon = document.getElementById("toggleIcon"+task.taskID);
                    icon.style.display = "none";
                    var taskCompleteContainer = document.getElementById("taskComplete"+task.taskID);
                    taskCompleteContainer.style.display = "block";
                    $scope.congratsCard = res.data.congratsCard;
                } else {
                    $scope.error = true;
                    $timeout(function() {
                        $scope.success = false;
                        $scope.error = false;
                    }, 30000); // remove error message from the dom after 30 secs
                }
            }, function(err) {
                $scope.success = false;
                $scope.error = true;
                $timeout(function() {
                    $scope.success = false;
                    $scope.error = false;
                }, 30000); // remove error message from the dom after 30 secs
            });
        }

        $scope.manualTasks = [{
            "taskTitle": "Fittness Challenge",
            "taskDescription": "Practice good posture for 5 minutes every day this week.",
            "taskUserManual": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            "TaskCompleteIndicator": "N",
            "taskID": 12345,
            "taskSequence": 1,
            "id": 1
        }, {
            "taskTitle": "Nutrition Challenge",
            "taskDescription": "Intentionally smile at someone everyday this week.",
            "taskUserManual": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            "TaskCompleteIndicator": "N",
            "taskID": 12346,
            "taskSequence": 2,
            "id": 2
        }, {
            "taskTitle": "Wellness Challenge",
            "taskDescription": "Make sure you eat one healthy meal a day this week.",
            "taskUserManual": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            "TaskCompleteIndicator": "N",
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