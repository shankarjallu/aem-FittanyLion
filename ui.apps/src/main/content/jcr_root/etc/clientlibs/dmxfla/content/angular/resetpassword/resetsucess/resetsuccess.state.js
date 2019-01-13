(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing resetsuccess component ...");
            $stateProvider
                .state('resetsuccess', {
                    url: '/resetsuccess',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/resetsuccess/jcr:content/section-content.html',
                    controller: 'ResetSuccess'


                });

    }]);
})();
