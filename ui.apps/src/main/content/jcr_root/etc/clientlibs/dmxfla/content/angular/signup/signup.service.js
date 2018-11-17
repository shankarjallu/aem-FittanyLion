//(function() {
//    function SignupService ($resource) {
//        console.log("initializing signup service..");
//
//        return $resource('/bin/fittany/userRegistration', {},{
//				'save': {
//        				method: 'POST',
//        				headers: {'Content-Type': 'applications/json'}
//    					}
//        });  
//    };
//
//SignupService.$inject = ['$resource'];
//angular.module('fittanyUiApp')
//       .factory('SignupService',SignupService);
//})();