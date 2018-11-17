(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing admin component ...");
            $stateProvider
                .state('admin', {
                    url: '/admin',
                    templateUrl: '/apps/hha/dmxfla/components/content/admin/admin.html',
                    controller: 'AdminController'

                });

    }]);
})();

