(function() {
    angular.module('fittanyUiApp')
        .controller("ProgressController", ['$scope', 'User', '$timeout',
            function($scope, User, $timeout) {
                console.log("This is in progress bar controller");
					

                    var c = document.getElementById("progressTracker");
                    //var grandEntries = 25; // this is fixed
                    //var midPointEntries = 12; // this is also fixed;
                    $scope.entries = User.getUser().taskTotalChancesCount || 0; // get total entries from user object once logged in

                    //outer circle grand prize
                    var ctx1 = c.getContext("2d");
                    ctx1.beginPath();
                    ctx1.arc(100, 100, 75, 0, 2 * Math.PI);
                    ctx1.lineWidth = 10;
                    ctx1.strokeStyle = "#edece8";
                    ctx1.stroke();

                    //grand prize tracker
                    var ctx2 = c.getContext("2d");
                    var grandEntries = $scope.entries;
                    var startStroke = 1;
                    var startGranPrizeTracker = function(){
                        if(grandEntries <= 0){
                            return;
                        }
                       // console.log("stroke #: " + startStroke);
                        $timeout(function(){
                            ctx2.beginPath();
                            ctx2.arc(100, 100, 75, 0, (2*Math.PI/25)*(startStroke));
                            ctx2.lineWidth = 10;
                            ctx2.strokeStyle = "#3a9de2";
                            ctx2.lineCap = "round";
                            ctx2.stroke();
                            startStroke++;
                            grandEntries--;
                            startGranPrizeTracker();
                        },100); 
                    };
                    startGranPrizeTracker();
                    

                    //inner circle mid point prize
                    var ctx3 = c.getContext("2d");
                    ctx3.beginPath();
                    ctx3.arc(100, 100, 65, 0, 2 * Math.PI);
                    ctx3.lineWidth = 10;
                    ctx3.strokeStyle = "#c9c8c3";
                    ctx3.stroke();

                    //mid point prize tracker
                    var ctx4 = c.getContext("2d");
                    var midPointEntries = $scope.entries;
                    var startStroke2 = 1;
                    var startMidPointTracker = function(){
                        if(midPointEntries <= 0){
                            return;
                        }
                       // console.log("stroke #: " + startStroke);
                        $timeout(function(){
                            ctx4.beginPath();
                            ctx4.arc(100, 100, 65, 0, (2*Math.PI/12)*(startStroke2));
                            ctx4.lineWidth = 10;
                            ctx4.strokeStyle = "#071f3a";
                            ctx4.lineCap = "round";
                            ctx4.stroke();
                            startStroke2++;
                            midPointEntries--;
                            startMidPointTracker();
                        },100); 
                    };
                    startMidPointTracker();
                    

            }
        ]);
}());