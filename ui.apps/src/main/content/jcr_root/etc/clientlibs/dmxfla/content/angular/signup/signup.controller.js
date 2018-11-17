(function() {


    angular.module('fittanyUiApp')
        .controller('SignupController',
            ['$scope', '$state', '$http', 'Base64',
                function($scope, $state, $http, Base64) {
                    console.log("This is signup controller");
                    $scope.state = $state;
                    $scope.radioAge = function(age) {
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

                    $scope.pennAlum = function(pennalum) {
                        if (pennalum == "pennAlum") {
                            $scope.palum = "inputcheckboxactive";
                            $scope.alumActive = "radiocircleactive"
                            $scope.npalum = "";
                            $scope.noalumActive = ""
                        } else if (pennalum == "noalum") {
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

                    $scope.submitFittanySignup = function(user) {
                        var formData = {};
                        var reset = {};
                        $scope.error = false;
                        $scope.success = false;

                        if (user) {


                            //This is for testing only.When we have auto Increment CustIdentifier in database,we need to remove this.
                            //No need to pass this field in Formdata object 

                            var custIdentifier = 7987690;

                            var custRecordMntdID = "1";


                            //make this into one work Y or N
                            var custPennStateUnivAlumniIN = "y";


                            formData = {
                                //CustomerIdentifer should be removed in Prod, just for dev testing.
                                "custIdentifier": custIdentifier,

                                "custFirstName": user.firstname,
                                "custLastName": user.lastname,
                                "custEmailAddress": user.email,
                                "custPassword": Base64.encode(user.password),
                                //  "custZip" : user.zip,
                                "custDOBRangeDesc": user.age,
                                "custPennStateUnivAlumniIN": custPennStateUnivAlumniIN,
                                "custRecordMntdID": custRecordMntdID

                            };




                            $http.post('/bin/getUserRegistrationServlet', JSON.stringify(formData)).then(function(response) {

                                if (response.data)

                                    $scope.msg = "Post Data Submitted Successfully!";

                                alert("success");

                            }, function(response) {

                                $scope.msg = "Service not Exists";

                                alert("Fail");



                            });




                        }
                    };


                }
            ]);
})();

