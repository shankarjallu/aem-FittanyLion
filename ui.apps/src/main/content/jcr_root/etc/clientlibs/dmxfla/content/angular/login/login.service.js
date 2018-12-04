(function() {
    angular.module('fittanyUiApp')

    .factory('AuthService', ['$http', '$timeout', 'Base64',
        function($http, $timeout, Base64) {
            var service = {};

            service.Login = function(email, password, callback) {

             //   var uri = 'http://localhost:5000/admin'; // replace with real uri in production
            	
            	var uri = "/bin/verifyUserLogin";

			
                var req = {
                        method: 'POST',
                        url: uri,
                       
                         data: {
                            "username": email,
                            "password": Base64.encode(password)
                        }
                    }
                    

//                //testing only
//                var req = {
//                        method: 'GET',
//                        url: uri + '?username=email&password=password',
//                        headers: {
//                            'Content-Type': 'application/json'
//                        }
//
//                    }


                  $http(req).then(function(res){
                    return callback(res);
                  },function(err){
                    return callback(err);
                  });

                   

            }


            return service;

        }
    ]);
})();