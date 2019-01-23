(function() {
    angular.module('fittanyUiApp')

    .factory('AuthService', ['$http', '$timeout', 'Base64',
        function($http, $timeout, Base64) {
            var service = {};

            service.Login = function(email, password, callback) {
               
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
                    return callback(res);
                  },function(err){
                    return callback(err);
                  });         

            }

            service.loginWithToken = function(token,callback) {
               

                var uri = '/bin/getUserSessionLogin'; // replace with real uri in production

                var req = {
                        method: 'GET',
                        url: uri + '?key='+ token

                    };


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