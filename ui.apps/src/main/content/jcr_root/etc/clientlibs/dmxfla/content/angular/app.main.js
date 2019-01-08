(function() {
angular.module('fittanyUiApp')
     .run(['$rootScope', '$location', '$state', 'Auth','SessionService', function ($rootScope, $location, $state, Auth, SessionService) {



        $rootScope.$on('$stateChangeStart', function (event,to,toParams,from,fromParams) {
          var auth = Auth.authorize();
          var state = to.name;
		  var sessionStarted = Auth.isSessionStarted();

            if(auth && !sessionStarted){
            angular.element(document).bind('mouseup mousedown mousewheel DOMMouseScroll keyup touchstart touchmove', function(event) {
              console.log("inside mouse event: step1");
              if(Auth.isSessionStarted()){
                SessionService.ResetTimer();
              }
              
            });
           		SessionService.giveSession();
           		Auth.setSession(true);
            }

        	if (!auth && (state != "login" && state != "signup" && state != "admin" && state !="root" && state !="rewards" && state !== "forgotpassword" && state != 'resetpassword')) {
          		console.log("redirect to home page..");
          		event.preventDefault();
          		$state.go("login");
        	} else if(auth && (state == "login" || state == "signup")){ // if user is already logged in send back to where they come from
            	event.preventDefault();
            	$state.go(from.name);
        	} 
      });
}]);
angular.module('fittanyUiApp')
	.config(['$urlRouterProvider', '$locationProvider',  function ($urlRouterProvider, $locationProvider) {
		$urlRouterProvider.otherwise('/');
        //$locationProvider.html5Mode(true); // remove hash from the url
	}]);


})();
