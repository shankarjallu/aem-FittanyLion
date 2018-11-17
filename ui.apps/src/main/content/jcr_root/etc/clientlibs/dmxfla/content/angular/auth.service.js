(function() {
    angular.module('fittanyUiApp')

    .factory('Auth', [
        function() {
           var service = {
                authorize:authorize,
                setAuth:setAuth,
                getAdminAuth: getAdminAuth,
                setAdminAuth: setAdminAuth
           };

           var auth = false;
           var adminLogin = false;

           function authorize(){
                return auth;
           }

           function setAuth(a){
                auth = a;
           }
           function setAdminAuth(a){
             adminLogin = a;
           }
           function getAdminAuth(){
            return adminLogin;
           }
            return service;

        }
    ]);
})();
