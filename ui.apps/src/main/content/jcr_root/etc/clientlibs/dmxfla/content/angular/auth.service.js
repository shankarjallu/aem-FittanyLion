(function() {
    angular.module('fittanyUiApp')

    .factory('Auth', [
        function() {
           var service = {
                authorize:authorize,
                setAuth:setAuth,
                getAdminAuth: getAdminAuth,
                setAdminAuth: setAdminAuth,
                setSession:setSession,
                isSessionStarted:isSessionStarted

           };
		   var sessionStarted = false;
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
             function setSession(s){
              sessionStarted = s;
           }
            function isSessionStarted(){
              return sessionStarted;
           }
            return service;

        }
    ]);
})();
