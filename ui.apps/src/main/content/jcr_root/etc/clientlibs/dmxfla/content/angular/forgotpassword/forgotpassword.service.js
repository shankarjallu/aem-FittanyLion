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
                          alert("scuiccess")
                      }else{
                           alert("fail");
                      }

                  },function(err){
                      alert("fail");
                  });

                   

            }


            return service;

        }
    ]);
})();