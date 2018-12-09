(function() {
    function SignupService ($http, $q) {
        console.log("initializing signup service..");
		var service = {
        	"registerUser": registerUser
        }
      //  var uri = 'http://localhost:5000/posts'; // change this to real uri
		
		var uri =  "/bin/getUserRegistrationServlet";

        function registerUser(data){
        	var deferred = $q.defer();
            var req = {
                method: 'POST',
                data: data,
                url: uri
               

            };

            $http(req).then(function(res) {
                 if (res.data.statusCode == 200) {
                         deferred.resolve(res);
                            } else {
                        deferred.reject(err);
                            }
                deferred.resolve(res);
            }, function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        }

        return service;

    };

SignupService.$inject = ['$http', '$q'];
angular.module('fittanyUiApp')
       .factory('SignupService',SignupService);
})();