(function() {

function NavbarController (Auth,$rootScope,$state, $scope, $window, $timeout, SessionService) {
      console.log("initializing navbar controller..");
        var vm = this;
        vm.isLoggedIn = false;
        vm.adminLoggedIn = Auth.getAdminAuth();
        //$scope.checkAdminInSession = $window.sessionStorage.getItem("adminIsLoggedIn") || false;
        $rootScope.$on('$stateChangeStart', function (event,to,toParams,from,fromParams) {
          vm.isLoggedIn = Auth.authorize();
        });

        $rootScope.$on('adminloggedin', function(event,data){
            vm.adminLoggedIn = Auth.getAdminAuth();
            //$scope.checkAdminInSession = $window.sessionStorage.getItem("adminIsLoggedIn") || false;
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
            $state.go("login");
        }
        vm.adminLogout = function(){
            Auth.setAdminAuth(false);
            vm.isNavbarCollapsed = true;
            //$window.sessionStorage.removeItem("adminIsLoggedIn");
            $rootScope.$broadcast("adminloggedin", false);
            $state.go("admin");
        }
};

NavbarController.$inject = ['Auth', '$rootScope', '$state' , '$scope', '$window', '$timeout', 'SessionService'];

angular.module('fittanyUiApp')
        .controller('NavbarController', NavbarController);
})();


