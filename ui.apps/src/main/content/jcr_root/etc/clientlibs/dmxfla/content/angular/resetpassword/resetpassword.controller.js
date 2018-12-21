(function() {


        angular.module('fittanyUiApp')
            .controller('ResetPassword',
                ['$scope', '$http', '$state', 'Base64', 'ResetPasswordService', '$filter',
                    function($scope, $state, Base64, $http, ResetPasswordService, $filter) {
                        console.log("This is  ForgotPassword  controller");

                        $scope.resetPassword = function(user) {
                              $scope.error = false;
                             $scope.success = false;


                            if (user) {
                                ResetPasswordService.Reset(user)
                                    .then(function success(response) {
                                        if (response.data.statusCode == 200) {
                                            alert("SUCCESSFULLY CHANGED THE EMAIL");
                                             $scope.success = true;
                                            $scope.error = false;


                                        } else {

                                            alert("NOT ABLE TO RESET PWD");

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