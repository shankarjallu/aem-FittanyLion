(function() {

 	addIndex();
 	mobileDesktopView();
 	findShowMore();


}());

function addIndex(){
    var trs = $(".table table tr");
    $(trs).each(function(index){
       			var tds = $(this).children("td");
       		    $(tds).each(function(index2){
                 	$(this).attr("index",index2);
       			 });


        });

}

function findShowMore(){
    $(".table .view-more").each(function(){
        showHideCols(this);
    });

}

function showHideCols(obj){
        if($(window).width() < 768 ) {
            obj.addView =  {nextIndex:2,maxViewIndex:1,viewType:"mobile",prevIndex:2};
        }else {
            obj.addView = {nextIndex:5,maxViewIndex:4,viewType:"desktop",prevIndex:5};
            }

 }

 function mobileDesktopView(){
     var allTables = $(".table-container");
    // console.log(allTables);
     var Tables = $(allTables).children("table");
     $(Tables).each(function(index){
         var table = this;
         var allCols = $(table).children("tbody").children("tr").children("td");
         if($(window).width() < 768 ) {
           $(allCols).each(function(index2){
             var cellIndex = this.cellIndex;
             if(cellIndex != 0 && cellIndex != 1){
                 	$(this).hide();
                 	 $(table).siblings(".view-more").show();
             }

       });

   	 	} else{
         	$(allCols).each(function(index){
             	var cellIndex = this.cellIndex;
             	if(cellIndex != 0 && cellIndex != 1 && cellIndex != 2 && cellIndex != 3 && cellIndex != 4){
                	$(this).hide();
                    $(table).siblings(".view-more").show();

                }else {
                    $(this).show();
                    $(this).css("width","15%");
                    $("table tr td:nth-child(1)").css("width","40%");

                }
          });
        }

     });

}


$(".table .view-more").click(function(event){
            event.preventDefault();
           // console.log(this);
            var view = this.addView;
    		//console.log(view);
            var tableId = $(this).parent(".table-container").attr("id");
        	var currentTable = $("#" + tableId + " table");
			var firstRowTds =  $(currentTable).children("tbody").children("tr:nth-child(1)").children("td");
            var totalTds =  firstRowTds.length;
			var cols = $(currentTable).children("tbody").children("tr").children("td");
            var freezeFirstColumn = $("#" + tableId + "span").attr("value");
            $(cols).each(function(index){
                if($(this).attr("index") == view.nextIndex){
						$(this).show();
                    if(view.viewType == "desktop"){
                        $(this).css("width","15%");
                    }


                }
                if(view.viewType == "mobile" && freezeFirstColumn == "true" && $(this).attr("index") == view.nextIndex-1 && view.nextIndex < totalTds){
						$(this).hide();

                }
                if(view.viewType == "mobile" && freezeFirstColumn == "false" && $(this).attr("index") == view.nextIndex-2 && view.nextIndex < totalTds){
						$(this).hide();

                }
                 if(view.viewType == "desktop" && freezeFirstColumn == "true" && $(this).attr("index") == view.nextIndex-4 && view.nextIndex < totalTds){
						$(this).hide();

                }
                if(view.viewType == "desktop" && freezeFirstColumn == "false" && $(this).attr("index") == view.nextIndex-5 && view.nextIndex < totalTds){
						$(this).hide();

                }


       	 	});

          //  console.log("total cols: " + totalTds);

            if(view.nextIndex < totalTds){
                view.prevIndex = view.nextIndex;
           		 view.nextIndex++;
            }
           //  console.log(view);
            if(view.nextIndex == totalTds){
                $(this).hide();
                $(this).next().show();
            }

            // console.log("nextIndex: " + view.nextIndex);
			if(view.prevIndex < view.nextIndex && view.nextIndex != totalTds){
                $(this).next().show();
            }

    });

 $(".table .view-less").click(function(event){
            event.preventDefault();
     		var viewMore = $(this).prev();
            var view = viewMore[0].addView;
     		//console.log(view);
			var tableId = $(this).parent(".table-container").attr("id");
        	var currentTable = $("#" + tableId + " table");
			var cols = $(currentTable).children("tbody").children("tr").children("td");
         	var freezeFirstColumn = $("#" + tableId + "span").attr("value");
            $(cols).each(function(index){
                if($(this).attr("index") == view.prevIndex && (view.prevIndex > view.maxViewIndex) ){
						$(this).hide();

                }

                if(view.viewType == "mobile" && freezeFirstColumn == "true" && $(this).attr("index") == view.prevIndex-1 && view.prevIndex > view.maxViewIndex){
						$(this).show();

                }
                if(view.viewType == "mobile" && freezeFirstColumn == "false" && $(this).attr("index") == view.prevIndex-2 && view.prevIndex > view.maxViewIndex){
						$(this).show();

                }
                if(view.viewType == "desktop" && freezeFirstColumn == "true" && $(this).attr("index") == view.prevIndex-4 && view.prevIndex > view.maxViewIndex){
						$(this).show();

                }
                if(view.viewType == "desktop" && freezeFirstColumn == "false" && $(this).attr("index") == view.prevIndex-5 && view.prevIndex > view.maxViewIndex){
						$(this).show();

                }
       	 	});


        // console.log("previndex: " + view.prevIndex);

         if(view.prevIndex > view.maxViewIndex){
             view.nextIndex = view.prevIndex;
             view.prevIndex--;
         }
        //  console.log(view);
         if(view.prevIndex == view.maxViewIndex){
                 $(this).hide();
                $(this).prev().show();
            } 

		 if(view.prevIndex < view.nextIndex && view.prevIndex != view.maxViewIndex){
                $(this).prev().show();
            }


    });

