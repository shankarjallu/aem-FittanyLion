(function() {

    var progressComponent = {

        controller: 'ProgressController',
        template:  '<div>' +
	'<div class="row gray-border borderShadow-sm">' +
		'<h3 style="text-align: center;">Your Progress</h3>' +
		'<div class="col-xs-7">' +
			'<div class="canvas-container">' +
				'<canvas id="progressTracker" width="200" height="200" style=""> Your browser does not support some features to display here! </canvas>' +
				'<div class="canvas-fill-text">' +
					'<span>You have:</span>' +
              '<br>' +
					'<span style="font-size: 40px" ng-class="{entriesMargin: entries < 10}">{{entries}}</span>' +
					 '<br>' +
					'<span style="margin-left:7px;">Entries</span>' +
				'</div>' +
			'</div>' +
		'</div>' +
		'<div class="col-xs-5">' +
			'<h4 style="font-size: 12px;"><i class="dot"></i> Grand Prize</h4>' +
			'<p style="margin-left:15px;">25 total entries for grand prize </p>' +
			'<h4 style="font-size: 12px;"><i class="dot_new" ></i> Mid-point Prize</h4>' +
			'<p style="margin-left:15px;">12 total entries for mid-point prize </p>' +
		'</div>' +
	'</div>' +
'</div>' 


};

angular.module('fittanyUiApp')
        .component('progressCircle', progressComponent);
})();
