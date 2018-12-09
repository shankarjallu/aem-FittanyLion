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
                            "username": Base64.encode(email),
                            "password": Base64.encode(password)
                        }
                    }
                    

                  $http(req).then(function(res){

                      if(res.data.statusCode == 200){ 
                          return callback(res);
                      }else{
                           return callback(err);
                      }

                  },function(err){
                    return callback(err);
                  });

                   

            }


            return service;

        }
    ]);
})();