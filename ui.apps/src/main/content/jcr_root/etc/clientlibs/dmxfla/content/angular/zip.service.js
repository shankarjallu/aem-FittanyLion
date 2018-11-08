
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

angular.module('fittanyUiApp')
       .factory('ZipService',ZipService);
})();