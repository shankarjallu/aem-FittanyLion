(function() {
    angular.module('fittanyUiApp')

    .factory('User', ['$http', '$q', '$rootScope',
        function($http, $q, $rootScope) {
            var service = {
                getUser: getUser,
                setUser: setUser,
                updateTask: updateTask,
                getTasks: getTasks,
                setTasks: setTasks
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
            // function updateTasksList(tasks){
            //     tasksList = tasks;
            //     $rootScope.$broadcast("tasklistUpdated", true); // tell listener on dom that tasklist is updated
            // }
            function updateTask(task) {
                if (user) {
                    var deferred = $q.defer();
                    var taskStartDate = user.taskStartDate.replace(/-/g, "/");
                    var taskEndDate = user.taskEndDate.replace(/-/g, "/");
              //      var uri = 'http://localhost:5000/tasks/' + task.id; // replace with real uri in production
                  //  taskStartDate": "14/1/2019",
                    //     "taskEndDate": "20/1/2019",
                    
                         var uri = '/bin/usertaskstatus';
                       
                         var req = {
                                 method: 'POST',
                                 url: uri,
                                 data: {
                                     "customerId": user.customerId,
                                     "taskStartDate":taskStartDate,
                                     "taskEndDate": taskEndDate,
                                      "custTaskCompleteIndicator": "Y",
                                      "taskID": task.taskID

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