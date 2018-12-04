(function() {
    angular.module('fittanyUiApp')

    .factory('SessionService', ['$timeout','$rootScope', '$state','$uibModal', 'Auth',
        function($timeout, $rootScope, $state, $uibModal, Auth) {
           var service = {
              giveSession: giveSession,
              ResetTimer: ResetTimer

           };
          var sessionLimit = 900000; // 15 min

          var startSession = null;

            function giveSession(){
              console.log("session given : step4")
              startSession = $timeout(function(){
                  TimerLogout();
              },sessionLimit);
            }

            function TimerLogout(){
              $uibModal.open({
                            templateUrl: '/apps/hha/dmxfla/components/content/session/keep-alive.html',
                            size: 'lg',
                            backdrop: false,
                            controller: ['$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
                                var vm = this;
                                vm.countdown = 60; 
                                var oneminTimer;
                                var startTimer = function(){
                                    vm.countdown--;
                                    if(vm.countdown == 0){
                                      vm.cancel();
                                      return;
                                    }
                                    oneminTimer = $timeout(function(){
                                      startTimer();
                                    },1000);
                                };
                                startTimer();
                                
                                vm.ok = function() {
                                    $uibModalInstance.dismiss('ok');
                                    sessionLimit = 900000; // give another 15 mins
                                    $timeout.cancel(oneminTimer);
                                    ResetTimer();
                                     
                                };
                                vm.cancel = function() {
                                    $uibModalInstance.dismiss('cancel');
                                    Auth.setAuth(false);
                                    Auth.setSession(false);
                                    $timeout.cancel(oneminTimer);
                                    ResetTimer();
                                    $state.go("login");
        
                                };
                            }],
                            controllerAs: 'vm'
                        });
           }
           function ResetTimer(){
            console.log("timer reset: step2");
              $timeout.cancel(startSession); // cancel the started timer
               // and start the new timer if still logged in
               if(Auth.authorize()){
                console.log("timer started: step3");
                 giveSession();
               }

           }

            return service;

        }
    ]);
})();
