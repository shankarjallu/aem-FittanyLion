(function() {
    angular.module('fittanyUiApp')

    .factory('User', ['$http', '$q', '$rootScope',
        function($http, $q, $rootScope) {
            var service = {
                getUser: getUser,
                setUser: setUser,
                updateTask: updateTask,
                getTasks: getTasks,
                setTasks: setTasks,
                updateTasksList,updateTasksList

            };

            var user = null;
            var tasksList = null;

            function getUser() {
                return user;
            }

            function setUser(u) {
                user = u;
                
            }

            function getTasks() {
                return tasksList;
                
            }
            function setTasks(tasks) {
                tasksList = tasks;
            }
            // update the taskslist once user indicate the complete task
            function updateTasksList(tasks){
                tasksList = tasks;
                $rootScope.$broadcast("tasklistUpdated", true); // tell listener on dom that tasklist is updated
            }
            function updateTask(task) {
                if (user.length) {
                    var deferred = $q.defer();
                    var uri = 'http://localhost:5000/tasks/' + task.id; // replace with real uri in production
                    var req = {
                        method: 'PUT',
                        data: {
                                "taskTitle": task.taskTitle,
                                "taskDescription": task.taskDescription,
                                "taskUserManual": task.taskUserManual,
                                "TaskCompleteIndicator": "Y",
                                "taskID": task.taskID,
                                "taskSequence": task.taskSequence
                        },
                        url: uri,
                        headers: {
                            'Content-Type': 'application/json'
                        }

                    };


                    $http(req).then(function(res) {
                        deferred.resolve(res);
                    }, function(err) {
                        deferred.reject(err);
                    });
                    return deferred.promise;
                }
            }

            return service;

        }
    ]);
})();