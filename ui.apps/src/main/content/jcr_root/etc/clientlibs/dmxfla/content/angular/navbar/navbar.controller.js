(function() {

function NavbarController (Auth,$rootScope,$state, $scope, $window, $timeout, SessionService) {
      console.log("initializing navbar controller..");
        var vm = this;
       // vm.version = VERSION;
       vm.isLoggedIn = Auth.authorize();
        vm.adminLoggedIn = false;
        $scope.checkAdminInSession = $window.sessionStorage.getItem("adminIsLoggedIn") || false;
        //checks login state once the state changes
        $rootScope.$on('$stateChangeStart', function (event,to,toParams,from,fromParams) {
          vm.isLoggedIn = Auth.authorize();
        });
        //checks if user is logged in using token
        $rootScope.$on('authWithToken', function (event,data) {
          vm.isLoggedIn = data;
        });

        $rootScope.$on('adminloggedin', function(event,data){
            vm.adminLoggedIn = data;
            $scope.checkAdminInSession = $window.sessionStorage.getItem("adminIsLoggedIn") || false;
        });

        vm.isNavbarCollapsed = true;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
            if(!vm.isNavbarCollapsed)  { document.getElementById("fittany-body").style.position="fixed"; }
            else { document.getElementById("fittany-body").style.position="";}
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

        vm.logout = function(){
            Auth.setAuth(false);
           // Auth.cancelTimeout();
            Auth.setSession(false);
            SessionService.ResetTimer();
            vm.isNavbarCollapsed = true;
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            $state.go("login");
        }
        vm.adminLogout = function(){
            Auth.setAdminAuth(false);
            vm.isNavbarCollapsed = true;
            $rootScope.$broadcast("adminloggedin", false);
            $window.sessionStorage.removeItem("adminIsLoggedIn");
           // $rootScope.$broadcast("adminIsLoggedIn", false);
            $state.go("root");
        }
};

NavbarController.$inject = [ 'Auth', '$rootScope', '$state' , '$scope', '$window', '$timeout', 'SessionService'];

angular.module('fittanyUiApp')
        .controller('NavbarController', NavbarController);
})();


