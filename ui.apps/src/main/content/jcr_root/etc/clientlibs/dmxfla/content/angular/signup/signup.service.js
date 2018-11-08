(function() {
    function SignupService ($resource) {
        console.log("initializing signup service..");

        return $resource('/bin/getUserRegistrationServlet'
//                    {
//                            update: {method:'POST'},
//                            
//                    }
        );  
    };

SignupService.$inject = ['$resource'];
angular.module('fittanyUiApp')
       .factory('SignupService',SignupService);
})();