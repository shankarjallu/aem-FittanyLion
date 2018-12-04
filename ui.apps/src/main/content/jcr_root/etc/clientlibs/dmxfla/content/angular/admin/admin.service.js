(function() {
    function AdminService($http, $q) {
        console.log("initializing admin service..");
        var service = {
            "loginAdmin": loginAdmin,
            "submitQuestions": submitQuestions
        }

        //admin login end point, change the url to real endpoint
        function loginAdmin(user,pass) {
			var uri = 'http://localhost:5000/admin'; // change this to real uri
            var deferred = $q.defer();
            var req = {
                method: 'GET',
                url: uri + '?username=user&password=pass',
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

		//admin questions, change the url to real endpoint
        function submitQuestions(questions){
          //  var uri = 'http://localhost:5000/questions'; // change this to real uri
        	
        	var uri = "/bin/insertTasksIntoDB";
			var deferred = $q.defer();
            var req = {
                method: 'POST',
                data: questions,
                url: uri
               

            };

            $http(req).then(function(res) {
                deferred.resolve(res);
            }, function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        }

    	return service;


    };

	AdminService.$inject = ['$http', '$q'];

    angular.module('fittanyUiApp')
        .factory('AdminService', AdminService);
})();
