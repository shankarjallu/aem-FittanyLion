(function() {
    angular.module('fittanyUiApp')

    .factory('ForgotPasswordService', ['$http', '$timeout', 'Base64',
        function($http, $timeout, Base64) {
            var service = {};

            service.Login = function(email) {

                 var Uri = "/bin/getForgotPasswordServlet";
			
                 var req = {
                              method: 'POST',
                              url: Uri + '?emailId='+email
                          }
                    
                  $http(req).then(function(res){

                      if(res.data.statusCode == 200){ 
                          alert("Email Sent to the user")
                      }else{
                           alert("Email Not registered");
                      }

                  },function(err){
                      alert("Some Issues");
                  });

                   

            }


            return service;

        }
    ]);
})();