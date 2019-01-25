(function() {


    angular.module('fittanyUiApp')
        .controller('ProfileController', ['$scope', '$state', 'User', 'Base64',
            function($scope, $state, User, Base64) {
                console.log("This is profile controller");
                $scope.state = $state;
                var userInfo = User.getUser(); 
                $scope.user = {
                    firstname:userInfo.customerFirstName,
                    lastname:userInfo.customerLastName,
                    email:userInfo.customerEmailId,

                };

                var agegroup = "2564";
                $scope.radioAge = function(ageIn) {
                    var age = ageIn || agegroup;
                    if (age == "25below") {
                        $scope.below25 = "inputcheckboxactive";
                        $scope.above25 = "";
                        $scope.above65 = "";
                    } else if (age == "2564") {
                        $scope.below25 = "";
                        $scope.above25 = "inputcheckboxactive";
                        $scope.above65 = "";
                    } else if (age == "65above") {
                        $scope.below25 = "";
                        $scope.above25 = "";
                        $scope.above65 = "inputcheckboxactive";
                    }
                };

                var alumin = "Y";

                $scope.pennAlum = function(alumIn) {
                    var alum = alumIn || alumin;
                    if (alum == "pennAlum") {
                        $scope.palum = "inputcheckboxactive";
                        $scope.alumActive = "radiocircleactive"
                        $scope.npalum = "";
                        $scope.noalumActive = ""
                    } else if (alum == "noalum") {
                        $scope.palum = "";
                        $scope.npalum = "inputcheckboxactive";
                        $scope.alumActive = ""
                        $scope.noalumActive = "radiocircleactive"
                    }
                };

                var resetClass = function() {
                    $scope.below25 = "";
                    $scope.above25 = "";
                    $scope.above65 = "";
                    $scope.palum = "";
                    $scope.alumActive = "";
                    $scope.npalum = "";
                    $scope.noalumActive = "";
                };

                $scope.radioAge();
                $scope.pennAlum();


            }
        ]);
})();