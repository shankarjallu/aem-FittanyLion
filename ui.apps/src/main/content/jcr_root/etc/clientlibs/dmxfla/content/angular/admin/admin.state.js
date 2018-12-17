(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing admin component ...");
            $stateProvider
                .state('admin', {
                    url: '/admin',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/admin.html',
                    controller: 'AdminController'

                });

    }]);
})();

