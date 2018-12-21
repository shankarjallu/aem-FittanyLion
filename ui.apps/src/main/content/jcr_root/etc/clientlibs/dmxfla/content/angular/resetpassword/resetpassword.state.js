(function(){

angular.module('fittanyUiApp')
    .config(['$stateProvider', function($stateProvider) {
        console.log("initializing resetpassword component ...");
            $stateProvider
                .state('resetpassword', {
                    url: '/resetpassword',
                    templateUrl: '/content/digital-marketing/en/highmark/fittanylion/restpassword.html',
                    controller: 'ResetPassword'

                });

    }]);
})();
