(function() {

    angular.module('fittanyUiApp')

        .controller('AdminController', ['$scope', '$rootScope', '$state', '$http', '$q', '$timeout', '$uibModal', 'Auth', '$window',

            function($scope, $rootScope, $state, $http, $q, $timeout, $uibModal, Auth, $window) {

                console.log("initializing admin controller");

                $scope.state = $state;

                var scope = $scope;

                $scope.success = $window.sessionStorage.getItem("adminIsLoggedIn") || false;

                $scope.isloggedIn = $window.sessionStorage.getItem("adminIsLoggedIn") || false;

                if (!$scope.success) {

                    $scope.submitAdmin = function(user) {

                        $scope.error = false;

                        $scope.dataLoading = true;

                        if (user.username == "admin" && user.password == "admin") {

                            $scope.error = false;

                            $window.sessionStorage.setItem("adminIsLoggedIn", true);

                            $scope.success = true;

                      //      $scope.userdata = value;

                            $scope.isloggedIn = true;

                            $scope.dataLoading = false;

                            userfound = true;

                            Auth.setAdminAuth(true);

                            $rootScope.$broadcast("adminloggedin", true);

                        } else {

                            $scope.error = true;

                            $scope.success = false;

                            $scope.message = "username or password didn't match";

                            $scope.dataLoading = false;

                        }

 

                    };

                }

 

                //this is production level post call

                $scope.submitQuestion = function(question) {

                    var form = this.questionForm;

                    var formData = {

                        "taskStartDate": question.startDate.getMonth() + "/" + question.startDate.getDate() + "/" + question.startDate.getFullYear(),

                        "taskEndDate": question.endDate.getMonth() + "/" + question.endDate.getDate() + "/" + question.endDate.getFullYear(),

                        "tasks": [{

                            "taskTitle": question.fitnessTitle,

                            "taskDescription": question.fittnessDesc,

                            "taskSequence": 1

                        }, {

                            "taskTitle": question.nutritionTitle,

                            "taskDescription": question.nutritionDesc,

                            "taskSequence": 2

                        }, {

                            "taskTitle": question.wellnessTitle,

                            "taskDescription": question.wellnessDesc,

                            "taskSequence": 3

                        }]

 

                    };

                    var reset = {};

                    $scope.quesError = false;

                    $scope.quesSuccess = false;

                    $scope.questionLoading = true;

                    //$scope.questionForm


                            $http.post('/bin/insertTasksIntoDB', JSON.stringify(formData)).then(function(response) {

                                if (response.data)

                                    $scope.msg = "Post Data Submitted Successfully!";

                                alert("success");

                            }, function(response) {

                                $scope.msg = "Service not Exists";

                                alert("Fail");



                            });



               };

 

            }

        ]);

 

})();