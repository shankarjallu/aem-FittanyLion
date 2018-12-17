(function() {

	var navbarComponentConfig = {
    	templateUrl: '/apps/hha/dmxfla/components/content/navbar/navbar-template.html',
    	controller: 'NavbarController',
    	controllerAs:'vm'
};

angular.module('fittanyUiApp')
        .component('navbar', navbarComponentConfig);
})();