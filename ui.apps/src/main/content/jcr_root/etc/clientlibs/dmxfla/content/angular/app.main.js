(function() {
angular.module('fittanyUiApp')
     .run(['$rootScope', '$location', '$state', 'Auth', function ($rootScope, $location, $state, Auth) {
        
        $rootScope.$on('$stateChangeStart', function (event,to,toParams,from,fromParams) {
          var auth = Auth.authorize();
          var state = to.name;
        if (!auth && (state != "login" && state != "signup" && state != "admin" && state !="root" && state !="rewards")) {
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
	.config(['$urlRouterProvider', function ($urlRouterProvider) {
		$urlRouterProvider.otherwise('/');
	}]);


})();
