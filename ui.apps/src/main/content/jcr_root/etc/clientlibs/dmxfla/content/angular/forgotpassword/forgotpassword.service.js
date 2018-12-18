(function() {
    angular.module('fittanyUiApp')

    .factory('ForgotPasswordService', ['$http', '$timeout', 'Base64',
        function($http, $timeout, Base64) {
            var service = {};


       service.Login = function(email) {
           var url = '/bin/getForgotPasswordServlet';
                 url = url + '?emailId='+email;
      return $http.post(url);
  };

 return service;


        }
    ]);
})();







