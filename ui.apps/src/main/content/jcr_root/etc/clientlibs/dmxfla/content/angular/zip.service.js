
(function() {
    function ZipService ($resource) {
        console.log("initializing zip service..");

        return $resource('https://securetenv3.highmark.com/tibwzp/restServices/county/:zip', {zip:'@zip'}, {
        		'get':{
        			method: 'GET',
        			headers: {
        				'Content-Type': 'application/json'
        			},
        			ignoreLoadingBar: true,
        			transformResponse: function(data,headers,status){
        				if(status == 200){
        					var x2js = new X2JS();
        					var jsonData = x2js.xml2js(data);
        					console.log("parsed data: " + jsonData.CountiesMessage.payload.counties.stateAbbreviation);
        					return jsonData;
        				}
        				
        			}
        		}
        });  
    };

ZipService.$inject = ['$resource'];

function EmailService($http, $q) {
        var service = {
            "checkEmail": checkEmail
        };
        console.log("initializing email service..");
      //  var uri = 'http://localhost:5000/emailCheck';
        
        var uri = "/bin/validateEmail";

        function checkEmail(val) {
            var deferred = $q.defer();
            var req = {
                method: 'GET',
                url: uri + '?email='+val
               

            };
            $http(req).then(function(res) {
            	 if(res.data.statusCode == 200){
                     deferred.resolve(res);
                  }else{
                        deferred.reject(res);
                  }
            }, function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        }


        return service;


    };

 EmailService.$inject = ['$http', '$q'];

angular.module('fittanyUiApp')
        .factory('ZipService', ZipService)
        .factory('EmailService', EmailService);

})();