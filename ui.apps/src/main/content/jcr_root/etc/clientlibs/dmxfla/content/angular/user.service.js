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

                    var uri = '/bin/customertaskstatus';
                    var req = {
                        method: 'POST',
                        url: uri,
                        data: {
                            "CustomerId": user.customerId,
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





			
