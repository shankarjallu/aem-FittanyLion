(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing admin component ...");
            $stateProvider
                .state('admin', {
                    url: '/admin',
                    templateUrl: '/content/digitalmarketing/en/highmark/fittanylion/admin/jcr:content/section-content.html',
                    controller: 'AdminController'

                });

    }]);
})();

