(function() {


    angular.module('fittanyUiApp')
        .controller('DatePickerController', ['$scope',
            function($scope) {
                console.log("initializing date controller");

                Date.prototype.addDays = function(current, days) {
                    var date = current;
                    date.setDate(date.getDate() + days);
                    return date;
                };

                $scope.today = function() {
                    $scope.question = {
                        startDate: new Date()
                    };
                };
                // $scope.today();

                $scope.clear = function() {
                    $scope.question = {
                        startDate: null,
                        endDate: null
                    };
                };
                
                $scope.disableKeyboard = function(e){
                    e.preventDefault();
                };

                $scope.inlineOptions = {
                    showWeeks: false
                };

                $scope.dateOptions = {
                    dateDisabled: disabled,
                    minDate: new Date(),
                    formatYear: 'yy',
                    startingDay: 1
                };

                $scope.dateOptions2 = {
                    dateDisabled: disabled2,
                    minDate: new Date(),
                    formatYear: 'yy',
                    startingDay: 1
                };

                // Disable past date and already progressed dates
                function disabled(data) {
                    var date = data.date,
                        mode = data.mode;
                    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 2 ||
                        date.getDay() === 3 || date.getDay() === 4 ||
                        date.getDay() === 5 || date.getDay() === 6);
                }

                function disabled2(data) {
                    var date = data.date,
                        mode = data.mode;
                    return mode === 'day' && (date.getDay() === 1 || date.getDay() === 2 ||
                        date.getDay() === 3 || date.getDay() === 4 ||
                        date.getDay() === 5 || date.getDay() === 6);

                }

                $scope.popup = {
                    opened: false,
                    opened2: false
                };


                $scope.openDatePicker = function() {
                    $scope.popup.opened = true;
                };

                $scope.showEndDate = false;
                $scope.updateEndDate = function(){
                     $scope.showEndDate = true;
                     $scope.question.endDate = new Date().addDays(new Date($scope.question.startDate.getFullYear(), $scope.question.startDate.getMonth(), $scope.question.startDate.getDate()), 6);

                };

                $scope.openDatePicker2 = function() {
                    $scope.dateOptions2.maxDate = new Date().addDays(new Date($scope.question.startDate.getFullYear(), $scope.question.startDate.getMonth(), $scope.question.startDate.getDate()), 6);
                    $scope.dateOptions2.minDate = new Date($scope.question.startDate.getFullYear(), $scope.question.startDate.getMonth(), $scope.question.startDate.getDate());
                    $scope.popup.opened2 = true;
                    $scope.popup.opened2 = true;
                };

            }
        ]);
})();
