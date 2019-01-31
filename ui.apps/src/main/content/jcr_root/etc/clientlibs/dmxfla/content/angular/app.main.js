(function() {
    angular.module('fittanyUiApp')
        .run(['$rootScope', '$location', '$state', 'Auth', '$timeout', '$uibModal', 'SessionService', 'CookieService', 'AuthService', 'User', '$window', function($rootScope, $location, $state, Auth, $timeout, $uibModal, SessionService, CookieService, AuthService, User, $window) {

            $rootScope.$on('$stateChangeStart', function(event, to, toParams, from, fromParams) {
                var auth = Auth.authorize();
                authGate();

                function authGate() {
                    var state = to.name;
                    // give user 15 min before session expires or ask to keep alive
                    var sessionStarted = Auth.isSessionStarted();

                    if (auth && !sessionStarted) {
                        angular.element(document).bind('mouseup mousedown mousewheel DOMMouseScroll keyup touchstart touchmove', function(event) {
                            console.log("inside mouse event: step1");
                            if (Auth.isSessionStarted() && !Auth.isModalOpen()) {
                                SessionService.ResetTimer();
                            }

                        });
                        SessionService.giveSession();
                        Auth.setSession(true);
                    }

                    if (!auth && (state != "login" && state != "signup" && state != "admin" && state != "adminquestions" && state !="root" && state !="rewards" && state != "forgotpassword" && state != 'resetpassword')) {
                        console.log("redirect to login page..");
                        event.preventDefault();
                        $state.go("login");
                    } else if (auth && (state == "login" || state == "signup")) { // if user is already logged in send back to wherer they come from
                        event.preventDefault();
                        $state.go(from.name || "profile");
                    }
                }

            });

            windowOnload();
            function windowOnload() {
                console.log("page refresh");
                var hash = $window.location.hash;
                var _state = hash.split("#/")[1];
                var authToken = CookieService.getCookie("authToken") || "";
                if (authToken && authToken != "undefined") {
                    AuthService.loginWithToken(authToken, function(response) {
                        if (response.data.StatusCode == 200) {
                            console.log("authorized..");
                            User.setUser(response.data);
                            Auth.setAuth(true); 
                            //notify navigation bar for logged in using token 
                            $rootScope.$broadcast("authWithToken", true);
                            $state.go(_state);
                        }else {
                            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                        }

                    });

                    
                } 
            };

        }]);

})();