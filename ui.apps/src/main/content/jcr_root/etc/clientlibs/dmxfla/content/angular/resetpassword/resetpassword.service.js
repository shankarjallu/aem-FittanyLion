(function() {
    angular.module('fittanyUiApp')

    .factory('ResetPasswordService', ['$http', '$timeout', 'Base64', '$location',
        function($http, $timeout, Base64, $location) {
            var service = {};


       service.Reset = function(user) {
           var custnewpwd = Base64.encode(user.password);


    var outputJson = $location.search();
       var key = $location.search().key;



           var url = '/bin/getForgotPasswordServlet';
                 url = url + '?custnewPassword='+ custnewpwd + '&key=' + key;


      return $http.post(url);
  };

 return service;


        }
    ]);
})();







