(function() {


        angular.module('fittanyUiApp')
            .controller('ForgotPassword',
                ['$scope', '$http', '$state', 'Base64', 'ForgotPasswordService', '$filter',
                    function($scope, $state, Base64, $http, ForgotPasswordService, $filter) {
                        console.log("This is  ForgotPassword  controller");

                        $scope.submitForgotPassword = function(user) {
                              $scope.error = false;
                             $scope.success = false;
                            var userEmail = $filter('lowercase')(user.email);

                            if (user) {
                                ForgotPasswordService.Login(userEmail)
                                    .then(function success(response) {
                                        if (response.data.statusCode == 200) {
                                            $scope.success = true;
                                            $scope.error = false;


                                        } else {
                                            $scope.success = false;
                                            $scope.error = true;

                                        }

                                    },
                                    function error(response) {
                                           alert("Email Not Registered or some error");
                                    });
                        }




                    }

                }]);
})();