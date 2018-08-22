var pageRegion,regionMatch=0,isRegionalPage=0,isCorrectRegion=0,userRegionCookie;
$(window).load(function() 
{
   // executes when complete page is fully loaded, including all frames, objects and images

	function GetQueryStringParams(sParam)
        {
            var sPageURL = window.location.search.substring(1);
            var sURLVariables = sPageURL.split('&');
            for (var i = 0; i < sURLVariables.length; i++)
            {
                var sParameterName = sURLVariables[i].split('=');
                if (sParameterName[0] == sParam)
                {
                    return sParameterName[1];
                }
            }
        }

	var currentPath = window.location.pathname;
              var wcmMode = GetQueryStringParams('wcmmode');
            console.log("wcmmode:" +wcmMode);

            if(wcmMode){  setCookie("wcmmode", wcmMode);  }
    var getAEMmode=getCookie("wcmmode");

    if(getAEMmode!="edit" ||getAEMmode==undefined || getAEMmode==null){

     var l = getLocation(window.location.href);
     pageRegion = l.pathname.split("/").filter(Boolean);
     userRegionCookie = getCookie("userRegion");
     if(userRegionCookie) {changeBrandLogo();}


    //check if it is a regional page
    for(var i=0;i<pageRegion.length;i++)
    {
        if(pageRegion[i]=="central-pennsylvania" || pageRegion[i]=="western-pennsylvania" || pageRegion[i]=="west-virginia" || pageRegion[i]=="north-eastern-pennsylvania"||pageRegion[i]=="delaware")
        {  
            isRegionalPage=1;
            str = pageRegion[i].replace(/-/g, ' ').toLowerCase().replace(/\b[a-z]/g, function(letter) {  return letter.toUpperCase();});

            $("#modal-subheader").text("Please enter ZIP Code for "+str+" region.");
            break;
        }
        else
        {
			$("#modal-subheader").text("Enter your organization's ZIP code so we can show you personalized information.");
        }
    }

    //remove back to homepage link from modal-footer
     if(userRegionCookie!=null || isRegionalPage==1 ){document.getElementById("modalFooter").style.display="block";}
     else{document.getElementById("modalFooter").style.display="none";}

	//Check if user is cookied, if yes check if user is on correct page as per cookied region
     if(userRegionCookie!=null)
     {
         if(isRegionalPage==1)
         {
				for(var i=0;i<pageRegion.length;i++)
                {
                    if(pageRegion[i]!=userRegionCookie)
             		{     
						regionMatch=0;
                        //$("#myModal").modal({backdrop: "static",keyboard:false});
                    }
                    else
                    {
                        regionMatch=1;
                        break;
                    }
                }
             if(!regionMatch){$("#myModal").modal({backdrop: "static",keyboard:false});}
         }
     }
     else
     {
         $("#myModal").modal({backdrop: "static",keyboard:false});
     }
   }
});


//code to prevent entering alphabets in zip field
$("#txtZipCode").keydown(function(e){
    if (e.shiftKey || e.ctrlKey || e.altKey) { // if shift, ctrl or alt keys held down
                e.preventDefault();         // Prevent character input
            } else {
                var n = e.keyCode;
                if (!((n == 8)              // backspace
                        || (n == 46)                // delete
                        || (n >= 35 && n <= 40)     // arrow keys/home/end
                        || (n >= 48 && n <= 57)     // numbers on keyboard
                        || (n >= 96 && n <= 105))   // number on keypad
                        ) {
                    e.preventDefault();
                }
            }
});

$("#btnAlertClose").click(function(){
	$("#divInvalidZIP").fadeOut();      
});

var getLocation = function(href) {
    var l = document.createElement("a");
    l.href = href;
    return l;
};

//validate zipcode
var interval;
$("#txtZipCode").keyup(function(e){
    if (this.value.length ==5)
    {
		var myArray = getZipCodeArray();
        var validZip=0, userRegion;
        for (var i = 0; i < myArray.length; i++) {
            if (myArray[i].Zip == $("#txtZipCode").val()) {
                if(myArray[i].Region.toLowerCase()=="cpa") {userRegion="central-pennsylvania";}
                else if(myArray[i].Region.toLowerCase()=="wpa") {userRegion="western-pennsylvania";}
                else if(myArray[i].Region.toLowerCase()=="wv") {userRegion="west-virginia";}
                else if(myArray[i].Region.toLowerCase()=="nepa") {userRegion="north-eastern-pennsylvania";}
                else if(myArray[i].Region.toLowerCase()=="de") {userRegion="delaware";}

				if(isRegionalPage==1)
            	{
                    for(var i=0;i<pageRegion.length;i++)
                    {
                         if(pageRegion[i]==userRegion)
                        {
                            isCorrectRegion=1;
                            break;
                        }
                        else
                        {
                            isCorrectRegion=0;
                        }
                    }
                    if(isCorrectRegion){$("#myModal").modal("hide");}
                    else{
                            var z='<a href="#" class="close" aria-label="close" id="btnAlertClose1" onclick="stopCountdown();">&times;</a>';
                            z += 'This page is not available for this ZIP code. Please enter the ZIP code associated with your organization or you will be returned to HighmarkEmployer.com in <span id="seconds">7</span> seconds.';
                            $("#divZIPRedirect").html(z);
                            //$(z).appendTo("#divZIPRedirect");
                            $("#divZIPRedirect").fadeIn();
                            var closeSeconds=7;
                            interval = setInterval(function(){
                                $("#seconds").html(closeSeconds);
                                closeSeconds--;
                                
                                if(closeSeconds < 1){
                                    //popup.modal('hide');
                                    window.location.href = 'https://www.highmarkemployer.com/'; 
                                    clearInterval(interval);
                                }
                                
                            }, 1000)
               			 }
            	}//regional
                else
                {
                  $("#myModal").modal("hide");
                }
                validZip=1;
                break;
            }
            else
            {
                //alert("Invalid ZIP");
                validZip=0;            
            }
        }

        if(!validZip)
        {

				$("#divInvalidZIP").fadeIn();   
        }
        else{
            setCookie("userZIP",$("#txtZipCode").val());
            setCookie("userRegion",userRegion);
			changeBrandLogo();
        }
    }


});


function stopCountdown()
{
    clearInterval(interval);
    $("#divZIPRedirect").fadeOut();
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + 3600 * 1000);
    var expires = 'expires=' + d.toUTCString();
    document.cookie = cname + '=' + cvalue + '; ' + expires + '; path=/';
}


function getCookie(name) {
   var re = new RegExp(name + '=([^;]+)');
        var value = re.exec(document.cookie);
        return value !== null ? unescape(value[1]) : null;
}

$("#linkZIPpopup").click(function(){
    var closePopupButton='<button id="closePopupButton" type="button" class="close" data-dismiss="modal">&times;</button>';
	$("#closePopupButton").remove();
    $("#modal-header").prepend(closePopupButton);
    document.getElementById("modalFooter").style.display="none";
    $("#modal-subheader").text("Enter your organization's ZIP code so we can show you personalized information.");
  $('#myModal').modal('show'); 
});

function changeBrandLogo()
{
	//header logo change
	userRegionCookie = getCookie("userRegion");
    if(userRegionCookie=="central-pennsylvania"){$("#imgBrandLogo").attr("src","/content/dam/digital-marketing/highmark/b2b/highmark-bs-logo.png");}
    else if(userRegionCookie=="western-pennsylvania"){$("#imgBrandLogo").attr("src","/content/dam/digital-marketing/highmark/b2b/highmark-bcbs-logo.png");}
    else if(userRegionCookie=="west-virginia"){$("#imgBrandLogo").attr("src","/content/dam/digital-marketing/highmark/b2b/highmark-bcbs-west-virginia-logo.png");}
    else if(userRegionCookie=="north-eastern-pennsylvania"){$("#imgBrandLogo").attr("src","/content/dam/digital-marketing/highmark/b2b/highmark-bcbs-logo.png");}
    else if(userRegionCookie=="delaware"){$("#imgBrandLogo").attr("src","/content/dam/digital-marketing/highmark/b2b/highmark-bcbs-delaware-logo.png");}
}