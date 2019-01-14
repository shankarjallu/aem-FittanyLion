(function() {
    angular.module('fittanyUiApp')

    .factory('User', ['$http', '$q', '$rootScope',
        function($http, $q, $rootScope) {
            var service = {
                getUser: getUser,
                setUser: setUser,
                updateTask: updateTask


            };

            var user = null;
            var tasksList = null;

            function getUser() {
                return user;
            }

            function setUser(u) {
                user = u;
                
            }





            function updateTask(task) {

                console.log(user);
                if (user) {
                    var deferred = $q.defer();
                 //   var uri = 'http://localhost:5000/tasks/' + task.id; // replace with real uri in production
                //    "taskStartDate": "14/1/2019",
               //     "taskEndDate": "20/1/2019",
                    var uri = '/bin/usertaskstatus';
                    var req = {
                        method: 'POST',
                        url: uri,
                        data: {
                            "customerId": user.customerId,
                            "taskStartDate":user.taskStartDate,
                            "taskEndDate": user.taskEndDate,
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





			
