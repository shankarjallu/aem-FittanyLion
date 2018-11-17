
(function() {
angular.module('fittanyUiApp')
 .controller('AdminController', ['$scope', '$rootScope', '$state', 'AdminService', '$q', '$timeout', '$uibModal', 'Auth', '$window',
            function($scope, $rootScope, $state, AdminService, $q, $timeout, $uibModal, Auth, $window) {
                console.log("initializing admin controller");
                $scope.state = $state;
                var scope = $scope;
                $scope.success = $window.sessionStorage.getItem("adminIsLoggedIn") || false;
                 $scope.isloggedIn = $window.sessionStorage.getItem("adminIsLoggedIn") || false;
                if (!$scope.success) {
                    $scope.submitAdmin = function(user) {
                        var formData = {};
                        var reset = {};
                        $scope.error = false;
                        //$scope.success = false;
                        $scope.dataLoading = true;
                        if (user) {
                            // use this for production 
                            /**
                            formData = user;
                                var result = AdminService.login().save(formData);
                                result.$promise.then(function(res){
                                    $scope.error = false;
                                    $window.sessionStorage.setItem("adminIsLoggedIn", true);
                                    $scope.success = true;
                                    $scope.message = res;
                                    $scope.userdata = res.data;
                                    $scope.isloggedIn = true;
                                    $scope.dataLoading = false;
                                    console.log(res);
                                    Auth.setAdminAuth(true);
                                    $rootScope.$broadcast("adminloggedin", true);
                                    //$state.go("login");
                                    
                                },function(err){
                                    console.log("error occured while posting");
                                    console.log(err);
                                    $scope.error = true;
                                    $scope.success = false;
                                    $scope.message = err;
                                    $scope.dataLoading = false;
                                });
                                **/

                            //this is for test, mocking post, comment it on production
                            formData = user;
                            var deffered = $q.defer();
                            var results = AdminService.login().query();
                            var userfound = false; // setting initial flag to false
                            results.$promise.then(function(res) {
                                angular.forEach(res, function(value, key) {
                                    if (value.username == user.username && value.password == user.password) {
                                        $scope.error = false;
                                        $window.sessionStorage.setItem("adminIsLoggedIn", true);
                                        $scope.success = true;
                                        $scope.userdata = value;
                                        $scope.isloggedIn = true;
                                        $scope.dataLoading = false;
                                        console.log(res);
                                        userfound = true;
                                        Auth.setAdminAuth(true);
                                        $rootScope.$broadcast("adminloggedin", true);
                                        deffered.resolve();
                                        return;

                                    }
                                });
                                if (!userfound) {
                                    $scope.error = true;
                                    $scope.success = false;
                                    $scope.message = "username or password didn't match";
                                    $scope.dataLoading = false;
                                    deffered.reject();
                                }


                            }, function(err) {
                                $scope.error = true;
                                $scope.success = false;
                                $scope.message = err.status;
                                $scope.dataLoading = false;
                                deffered.reject();
                            });
                            return deffered.promise;


                        }
                    };
                }

                //this is production level post call 
                $scope.submitQuestion = function(question) {
                    var form = this.questionForm;
                     var formData = {
                        "startDate": question.startDate.getMonth() + "/" + question.startDate.getDate() + "/" + question.startDate.getFullYear(),
                        "endDate": question.endDate.getMonth() + "/" + question.endDate.getDate() + "/" + question.endDate.getFullYear(),
                        "tasks": [
                            {
                                "taskTitle": question.fitnessTitle,
                                "taskDescription": question.fittnessDesc,
                                "taskSequence": 1
                            },
                            {
                                "taskTitle": question.nutritionTitle,
                                "taskDescription": question.nutritionDesc,
                                "taskSequence": 2
                            },
                            {
                                "taskTitle": question.wellnessTitle,
                                "taskDescription": question.wellnessDesc,
                                "taskSequence": 3
                            }
                        ]
                        
                    };
                    var reset = {};
                    $scope.quesError = false;
                    $scope.quesSuccess = false;
                    $scope.questionLoading = true;
                    //$scope.questionForm 
                    if (question) {
                        $uibModal.open({
                            templateUrl: '/apps/hha/dmxfla/components/content/admin/confirmation-dialog.html',
                            size: 'sm',
                            controller: ['$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
                                var vm = this;
                                vm.startDate = question.startDate;
                                vm.endDate = question.endDate;
                                vm.ok = function() {
                                    $uibModalInstance.dismiss('ok');
                                   // $timeout(function() {
                                        var results = AdminService.questions().save(formData);
                                        results.$promise.then(function(res) {
                                            scope.quesError = false;
                                            scope.quesSuccess = true;
                                            scope.questionLoading = false;
                                            console.log(res);
                                            //$state.go("login");

                                        }, function(err) {
                                            console.log("error while posting!");
                                            console.log(err);
                                            scope.quesError = true;
                                            scope.quesSuccess = false;
                                            scope.quesMessage = err.status;
                                            scope.questionLoading = false;
                                        });
                                        form.$setPristine();
                                        form.$setUntouched();
                                        scope.question = angular.copy(reset);
                                        //question = angular.copy(resetform);
                                    //}, 500);
                                };
                                vm.cancel = function() {
                                    $uibModalInstance.dismiss('cancel');
                                    scope.questionLoading = false;
                                };
                            }],
                            controllerAs: 'vm'
                        });


                    }

                };


            }
        ]);


})();

