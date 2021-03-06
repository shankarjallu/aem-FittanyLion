(function() {

	var navbarComponentConfig = {

    	controller: 'NavbarController',
    	controllerAs:'vm',
        template: '<nav class="navbar navbar-default" role="navigation" style="margin-bottom: 0px;" ng-class="{headerCollapsed: vm.isNavbarCollapsed,headerExpanded:!vm.isNavbarCollapsed}">' +
                     '<div class="container">' +
         '<div class="navbar-header">' +
            '<button type="button" class="navbar-toggle" ng-click="vm.toggleNavbar()">' +
                '<span class="sr-only">Toggle navigation</span>' +
                 '<span class="icon-bar"></span>' +
                  '<span class="icon-bar"></span>' +
                  ' <span class="icon-bar"></span>' +
            '</button>' +
            '<a class="navbar-brand logo" href="#/">' +

                '<img id="logo-fittany" ng-class="{logoCollapsed: vm.isNavbarCollapsed,logoExpanded:!vm.isNavbarCollapsed}" alt="Fiitany logo">' +
           '</a>' +
        '</div>' +
        '<div id="sidenav" class="sidenav" ng-class="{sidenavCollapsed: vm.isNavbarCollapsed,sidenavExpanded:!vm.isNavbarCollapsed}" uib-collapse="" style="">' +
            '<ul class="nav navbar-nav">' +
                '<li ui-sref-active="active">' +
                    '<a ui-sref="login" href="" ng-click="vm.toggleNavbar()" ng-if="!vm.isLoggedIn">' +
                        '<span>Login</span>' +
                    '</a>' +
                  '</li>' +
             '<li ui-sref-active="active">' +
                    '<a ui-sref="profile" href="" ng-click="vm.toggleNavbar()" ng-if="vm.isLoggedIn">' +
                        '<span>Profile</span>' +
                    '</a>' +
                '</li>' +
                 
                '<li ui-sref-active="active">' +
                    '<a ui-sref="tasks" href="" ng-click="vm.toggleNavbar()" ng-if="vm.isLoggedIn">' +
                        '<span>Challenges</span>' +
                    '</a>' +
                '</li>' +
                '<li ui-sref-active="active">' +
                    '<a ui-sref="rewards" href="" ng-click="vm.toggleNavbar()">' +
                        '<span>Rules & Prizes</span>' +
                    '</a>' +
                '</li>' +
                
                '<li ui-sref-active="active" >' + 
                    '<a  ng-click="vm.logout()" ng-if="vm.isLoggedIn">' +
                        '<span>Log Out</span>' +
                    '</a>' +
                '</li>' +

           ' </ul>' +

        '</div>' +
    '</div>' +
'</nav>'


};


    angular.module('fittanyUiApp')
        .component('navbar', navbarComponentConfig);
})();



