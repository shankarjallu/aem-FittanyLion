(function() {

	var navbarComponentConfig = {
    	templateUrl: '/apps/hha/dmxfla/components/content/navbar/header.html',
    	controller: 'NavbarController',
    	controllerAs:'vm'
};

angular.module('fittanyUiApp')
        .component('navbar', navbarComponentConfig);
})();