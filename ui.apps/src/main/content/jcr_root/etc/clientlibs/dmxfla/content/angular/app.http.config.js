 (function() {

	angular.module('fittanyUiApp')
	.config(['$httpProvider', function ($httpProvider) {
		$httpProvider.interceptors.push('httpInterceptor');
	}]);
	
})();