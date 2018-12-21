(function() {
    angular.module('fittanyUiApp')

    .factory('ResetPasswordService', ['$http', '$timeout', 'Base64',
        function($http, $timeout, Base64) {
            var service = {};


       service.Reset = function(user) {
           var custnewpwd = Base64.encode(user.password);
           var url = '/bin/getForgotPasswordServlet';
                 url = url + '?custnewPassword='+ custnewpwd;


      return $http.post(url);
  };

 return service;


        }
    ]);
})();







