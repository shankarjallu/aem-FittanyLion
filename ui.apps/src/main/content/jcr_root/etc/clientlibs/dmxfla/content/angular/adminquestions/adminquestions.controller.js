
(function() {
    angular.module('fittanyUiApp')
        .controller('AdminQuestionsController', ['$scope', '$rootScope', '$state', 'AdminService', '$q', '$timeout', '$uibModal', 'Auth', '$window',
            function($scope, $rootScope, $state, AdminService, $q, $timeout, $uibModal, Auth, $window) {
                console.log("initializing admin controller");
                $scope.state = $state;
                var scope = $scope;


                //this is production level post call 
                $scope.submitQuestion = function(question) {
                    var form = this.questionForm;
                    var taskManualDescription = "This is Task Maual Desc";  // Delete this when u have this field
                    var formData = {
                    		"taskStartDate": question.startDate.getDate()  + "/" +  (question.startDate.getMonth() + 1) + "/" + question.startDate.getFullYear(),
                            
                           "taskEndDate": question.endDate.getDate()  + "/" + (question.endDate.getMonth()+ 1) + "/" + question.endDate.getFullYear(),

                        "tasks": [{
                            "taskTitle": question.fitnessTitle,
                            "taskDescription": question.fittnessDesc,
                        "taskManualDescription": taskManualDescription,

                            "taskSequence": 1
                        }, {
                            "taskTitle": question.nutritionTitle,
                            "taskDescription": question.nutritionDesc,
                                "taskManualDescription": taskManualDescription,  //this will be dynamic value
                            "taskSequence": 2
                        }, {
                            "taskTitle": question.wellnessTitle,
                            "taskDescription": question.wellnessDesc,
                                "taskManualDescription": taskManualDescription,
                            "taskSequence": 3
                        }]

                    };
                    var reset = function () {
						question.startDate = "";
                        question.endDate = "";
                        question.taskTitle = "";
                        question.taskDescription = "";
                        question.fittnessDesc = "";
                        question.fitnessTitle = "";
                        question.nutritionTitle = "";
                        question.nutritionDesc = "";
                        question.wellnessTitle = "";
                        question.wellnessDesc = "";
                        question.trivia = "";
                    };
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

                                    var submitQuestions = AdminService.submitQuestions(JSON.stringify(formData));
                        				submitQuestions.then(function(response) {
                        				    if(response.data.statusCode == 200){
                                                console.log("questions submitted");
                                			scope.quesError = false;
                                        	scope.quesSuccess = true;
                                        	scope.questionLoading = false;
                                            form.$setPristine();
                                    		form.$setUntouched();
                                    		reset();

                                            }else{
                                                console.log("error submitting questions");
                                			scope.quesError = true;
                                        	scope.quesSuccess = false;
                                        	scope.quesMessage = err.status;
                                        	scope.questionLoading = false;


                                            }

                        				},function(err) {
                            				console.log("error occurred while submitting..");
                            				scope.quesError = true;
                                        	scope.quesSuccess = false;
                                        	scope.quesMessage = err.status;
                                        	scope.questionLoading = false;
                        				});

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