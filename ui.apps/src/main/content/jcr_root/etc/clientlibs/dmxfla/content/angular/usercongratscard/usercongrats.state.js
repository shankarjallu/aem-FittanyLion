(function() {

    var congratsComponent = {
   
        controller:'CongratsController',
        
        template: '<div class="congrats" id="congrats">' +
	'<div class="gray-border borderShadow-sm" style="margin-left: 25px; margin-right: 25px; padding-bottom: 50px;">' +
		'<span class="x-congrats" ng-click="removeBanner()">X</span>' +
		'<div class="container">' +
			'<div class="congrats-image-container">' +
				'<img src="/content/dam/digital-marketing/en/highmark/fittanylion/Congratulations_Lion_Mobile.png" alt="congrats-logo">' +
				
			'</div>' +
			'<div class="congrats-text-container">' +
				'<h3>Congratulations</h3>' +
				'<h4>Your hadwork has paid off!</h4>' +
			'</div>' +
			
		'</div>' +
		'<div id="congrats-toggle-container" class="collapse">' +
			'<hr class="congrats-hr congrats-hr-left">' +
			'<hr class="congrats-hr congrats-hr-right">' +
			'<div class="well" style="background-color:#fff;border:none;text-align: center;">' +
			'<p>You will recieve an email shortly to claim</p>' +
			
			'</div>' +
		'</div>' +
	'</div>' +
	'<a data-toggle="collapse" href="" ng-href="" data-target="#congrats-toggle-container" id="congratsToggle" ng-click="toggleCongrats()"><span class="glyphicon glyphicon-chevron-down toggleTaskIcon" style="background-color: #3a9de2;color:#fff;"></span></a>' +
	
'</div>'
	
};

angular.module('fittanyUiApp')
        .component('congratsComponent', congratsComponent);
})();
    