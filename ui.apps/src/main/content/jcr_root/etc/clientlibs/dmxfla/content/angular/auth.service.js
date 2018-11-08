(function() {
    angular.module('fittanyUiApp')

    .factory('Auth', [
        function() {
           var service = {
                authorize:authorize,
                setAuth:setAuth
           };
           var auth = false;

           function authorize(){
                return auth;
           }

           function setAuth(val){
                auth = val;
           }
            return service;

        }
    ]);
})();
