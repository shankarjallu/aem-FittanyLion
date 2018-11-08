(function() {

function NavbarController (Auth,$rootScope,$state) {
      console.log("initializing navbar controller..");
        var vm = this;
        vm.isLoggedIn = false;
        $rootScope.$on('loggedin', function (event,data) {
          vm.isLoggedIn = Auth.authorize();
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
            vm.isNavbarCollapsed = true;
            $rootScope.$broadcast("loggedin", false);
            $state.go("login");
        }
};

NavbarController.$inject = ['Auth', '$rootScope', '$state'];

angular.module('fittanyUiApp')
        .controller('NavbarController', NavbarController);
})();


