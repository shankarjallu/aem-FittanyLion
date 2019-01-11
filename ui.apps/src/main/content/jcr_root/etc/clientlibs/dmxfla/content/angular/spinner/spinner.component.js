(function() {

	var loaderComponentConfig = {
    	template: '<div class="lds-default" ><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>'
};

angular.module('fittanyUiApp')
        .component('loader', loaderComponentConfig);
})();