(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing admin component ...");
            $stateProvider
                .state('adminquestions', {
                    url: '/adminquestions',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/adminquestions/jcr:content/section-content.html',
                    controller: 'AdminQuestionsController'

                });

    }]);
})();

