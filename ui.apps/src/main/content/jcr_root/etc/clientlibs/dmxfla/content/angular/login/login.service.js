(function() {
    angular.module('fittanyUiApp')

    .factory('AuthService', ['$http', '$timeout', 'Base64',
        function($http, $timeout, Base64) {
            var service = {};

            service.Login = function(email, password, callback) {

                var uri = '/bin/verifyUserLogin'; // replace with real uri in production

			
                var req = {
                        method: 'POST',
                        url: uri,
                        headers: {
                            'Content-Type': 'application/json'
                        },
                         data: {
                            "username": email,
                            "password": Base64.encode(password)
                        }
                    }
                    

           


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