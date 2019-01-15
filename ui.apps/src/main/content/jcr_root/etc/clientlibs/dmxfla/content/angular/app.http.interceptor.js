(function() {

    function httpInterceptor () {
        var service = {
            request: request,
            response: response,
            responseError:responseError
        };

        return service;

        function request (config) {
            console.log("inside request interceptor");
            config.headers = config.headers || {};
            config.headers['Access-Control-Allow-Origin'] =  "*";
            // config.headers['Access-Control-Allow-Headers:'] = "*";
            // config.headers['Access-Control-Allow-Methods'] = "*";
            document.getElementById("overlay").style.display = "block";

            return config;
        }
        function response (config) {
            console.log("inside response interceptor");
           // config.headers['Access-Control-Allow-Origin'] =  "*";
           document.getElementById("overlay").style.display = "none";
            return config;
        }
        function responseError(reject){
            console.log("inside response error interceptor");
           // config.headers['Access-Control-Allow-Origin'] =  "*";
           document.getElementById("overlay").style.display = "none";
            return reject;
        }
    }
    angular
        .module('fittanyUiApp')
        .factory('httpInterceptor', httpInterceptor);
})();

