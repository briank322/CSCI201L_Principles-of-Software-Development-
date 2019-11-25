<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script
  src="http://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<title>Profile</title>

</head>
<style>
body{
margin:0;
padding:0;
}
#glassimage{
padding-left: 15px;
padding-top: 2px;
padding-right:20px;


} 

#glass{
width: 80px;
height: 28px;
background-color: gray;

border-style: solid;
  border-color: darkgray;
  padding-left: 10px;


}

.container2{
width: 100%;

}
table
{
border-collapse: collapse;
width: 100%;
padding-top: 10px;

}

tr{
border-bottom: 1pt solid black;
padding-top: 10px;
width: 100%;
}

th, td {
  padding-left:50px;	
  padding-bottom: 15px;
  padding-top: 15px;
  text-align: left;
  font-size:14px;
}

body{
font-family:Arial;


}
.header{
width: 100%;
}


#bookworm{
height: 150px;
padding-left: 15%;
padding-right:5%;
width: 15%;
margin: 0;
float:left;
padding-top: 20px;
}

.container { 
 float:left;
 width:65%;
 padding-top: 100px;
  height: 150px;
  vertical-align: center;
}



.form-control{
font-size: 16px;
width: 300px;
padding: 10px 10px;
margin:0;
float: left;
}

#submit {
float: left;
padding-top: 6px;
padding-left: 20px;
padding-right:20px;
}

.btn{
font-size: 24px;
}

#radioleft{
float:left;
padding-right: 40px;
}

#radioright{
}


#firstline{
font-size: 42px;
text-align: center;
padding-top: 40px;

}

.table th, .table td {
	text-align: center;
	vertical-align: middle;
}

.row{
padding-left: 10%;
padding-right:10%;
}

#profile img{
width: 40px;
}

.vertical-center{
float:left;
width: 80%;
}

</style>
<body>

<div class = "header">
<a href='Loggedinhomepage.jsp'>
<img id="bookworm" src="image/bookworm.png"/>
</a>


<div class="container">
  <div class="vertical-center">
   <form name = "myform" method = "GET" action ="Validate" onsubmit = "return formvalidate();">

	    <input type="text" id = "keyword" name="keyword" class="form-control" 
	    id="search-id" placeholder="What book is on your mind?">
	       <div id="submit">
	    <div id = "glass">
	    <input id = "glassimage" type="image" src="image/magnifying_glass.png" alt="Submit" color="skyblue" height = "20px" width = "50px">
	     
	    </div>
	    </div>
	    
	    
	    <div id="radioleft">
		<div class="radios">
		<input type = "radio" id = "option1" class="line1" name = "option" value = ""> Name   
		</div>      
		<div class = "radios">
		<input type = "radio" id = "option2" class="line2" name = "option" value = "inauthor"> Author
		</div>
		</div>
		
		
		
		<div id="radioright">
		<div class="radios">
		<input type = "radio" id = "option3" class="line1" name = "option" value = "isbn"> ISBN 
		</div>      
		<div class = "radios">
		<input type = "radio" id = "option4" class="line2" name = "option" value = "inpublisher"> Publisher
		</div>
		</div>


	</form>
	<%=request.getAttribute("error2") == null? "":request.getAttribute("error2")%>
	<br>
	<%=request.getAttribute("error") == null? "":request.getAttribute("error")%>
	 
  </div>
  <div id = "profile">
  </div>
</div>
</div>


<hr id ="headline"/>

<div class = "row">

<div id = "firstline">

</div>
<hr id ="headline"/>
<div class = "container2">
<table>
<tbody>


</tbody>


</table>



</div>

</div>

<script>



//getCookie and check_Login was implemented by referrencing the following website.
//https://www.w3schools.com/js/js_cookies.asp
function getCookie(cookiename){
	
	var name = cookiename + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i< ca.length; i++){
		var c = ca[i];
		while(c.charAt(0) == ' '){
			c.substring(1);
		}
		if(c.indexOf(name)==0){
			return c.substring(name.length, c.length);
		}
	}
	return "";
	
	
}

function check_Login(){
	var username = getCookie("username");
	if(username != ""){
		document.getElementById("profile").innerHTML
		=  "<a href = \"Profile.jsp\"><img src=\"image/profile.jpg\" /></a>";
		document.getElementById("firstline").innerHTML = 
			getCookie("username")+"'s favorites: ";
	}
	return username;
};
function formvalidate() {
	
	var searchBox = document.getElementById("keyword").value;
	var optionBox1 = document.getElementById("option1");
	var optionBox2 = document.getElementById("option2");
	var optionBox3 = document.getElementById("option3");
	var optionBox4 = document.getElementById("option4");
	console.log(searchBox);
	
	if(searchBox=="" || searchBox==null){
		alert("Search Box is empty");
		return false;
	}
	else{
		return true;
	}
	
}
check_Login();


		
		
//GETTING a JSON and making an AJAX Function, I referenced the template code from 
//my current ITP class coursework and Professor's source code which is open to 
//All ITP 303 students. The github link for this code is the following
//https://github.com/nayeonk/itp303-am-fall-2019/tree/master/lect10-api




var x = document.cookie;
var y = x.substring(9);

$.ajax({
	async: false,
	url: 'ProfileValidate',
	data: {
		name: "profile",
		username: y
	},
	success: function(data){
		let temp = data;
		let favProfile = temp.split(",");
		displayResults(favProfile);
	}
}); 



function displayResults(favs){
	
	
	let tbodyElement = document.querySelector("tbody");

	while( tbodyElement.hasChildNodes()){
		tbodyElement.removeChild(tbodyElement.lastChild);
	}
	for(var i = 0; i < favs.length-1; i++)   {
		let endpoint = "https://www.googleapis.com/books/v1/volumes/"+favs[i];
		let xhr = new XMLHttpRequest();
		xhr.open("GET", endpoint, false);
		xhr.send();
		let results = JSON.parse(xhr.responseText);
		
		let trElement = document.createElement("tr");
		let tr = i.toString();
		console.log("tr!");
		console.log(tr);
		trElement.setAttribute("id", tr);
		
		//trElement.class = "border";
		let nailimage = document.createElement("a");
	 	
	 	nailimage.onclick = function(){
	 		//window.history.back();
	 		sessionStorage.setItem("bookdetail", results.selfLink);
	 	};
		nailimage.href = "Details.jsp";
		
		let cellContent = document.createElement("td");
		let cellThumbnail = document.createElement("td");
		
		let cellTitle = document.createElement("h1");
		let cellAuthor = document.createElement("h2");
		let cellPublisher = document.createElement("p");
		let cellSummary = document.createElement("p");
		let cellPubdate = document.createElement("p");
		
		let cellIsbn = document.createElement("p");
		
		
		//Create an <img> tag for cover image
		
		let imgElement = document.createElement("img");
		if(results.volumeInfo.imageLinks == null)
		{
			imgElement.src= "image/nimg2.png";	
		}
		else{
			imgElement.src = results.volumeInfo.imageLinks.smallThumbnail;

		}
		
		
		/////////////////////////////////////////////////////
		//favorite
		
		let cellremove = document.createElement("button");
		cellremove.innerHTML = "&#9733 Remove";
		cellremove.setAttribute ("style", "height: 50px; width: 130px; font-size: 20px;");
		cellremove.setAttribute ("id", "remove");
		/* if(fav){
			cellfavorite.style.display = "none";
		} */
		//console.log("hi");
		
		
		
		
		//cellfavorite.setAttribute('onclick','return alertfunction(results.id);');

		cellremove.onclick = function() {
			var bookid = results.id;
			if ( y == null || y == ""){
				alert("Please log in. Otherwise you can't use this feature");
			}
			$.ajax({
				async: false,
				url: 'DetailsValidate',
				data: {
					name: "remove",
					username: y,
					bookid: bookid
				}
			});
			console.log("i.string()");
			console.log(i.toString());
			document.getElementById(tr).style.display = "none";
		}
		nailimage.appendChild(imgElement);
		cellThumbnail.appendChild(nailimage);
		trElement.appendChild(cellThumbnail);
		
	
		
		//TITLE
		if(results.volumeInfo.title == null){
			cellTitle.innerHTML = "No Title Available";
		}
		else{
			cellTitle.innerHTML = results.volumeInfo.title;
		}
		
		//AUTHOR
		if(results.volumeInfo.authors == null){
			cellAuthor.innerHTML = "No Author Available!";
		}
		else{
			cellAuthor.innerHTML = ("Author: "+results.volumeInfo.authors).italics();
		}
		
		//PUBLISHER
		if(results.volumeInfo.publisher ==  null){
			cellPublisher.innerHTML = "No Publisher Available";
		}
		else{
			cellPublisher.innerHTML = ("Publisher: "+results.volumeInfo.publisher).bold();
		}
		
		//PUBLISHED DATE
		if(results.volumeInfo.publishedDate==null){
			cellPubdate.innerHTML = "No Published Date Available";
		}
		else{
		
			cellPubdate.innerHTML = ("Published Date: "+results.volumeInfo.publishedDate).bold();
		
			var month = results.volumeInfo.publishedDate.substring(5,7);
			var year = results.volumeInfo.publishedDate.substring(0,4);
			var date = results.volumeInfo.publishedDate.substring(8,10);
			console.log(month);
		
				if(month=="12"){
					cellPubdate.innerHTML = ("Published Date: "+"December"+" "+date+" "+year).bold();
				}
				else if(month == "11"){
					cellPubdate.innerHTML = ("Published Date: "+"November"+" "+date+" "+year).bold();
				}
				else if(month == "10"){
					cellPubdate.innerHTML = ("Published Date: "+"October"+" "+date+" "+year).bold();
				}
				else if(month == "09"){
					cellPubdate.innerHTML = ("Published Date: "+"September"+" "+date+" "+year).bold();
				}
				else if(month == "08"){
					cellPubdate.innerHTML = ("Published Date: "+"August"+" "+date+" "+year).bold();
				}
				else if(month == "07"){
					cellPubdate.innerHTML = ("Published Date: "+"July"+" "+date+" "+year).bold();
				}
				else if(month == "06"){
					cellPubdate.innerHTML = ("Published Date: "+"June"+" "+date+" "+year).bold();
				}
				else if(month == "05"){
					cellPubdate.innerHTML = ("Published Date: "+"May"+" "+date+" "+year).bold();
				}
				else if(month == "04"){
					cellPubdate.innerHTML = ("Published Date: "+"April"+" "+date+" "+year).bold();
				}
				else if(month == "03"){
					cellPubdate.innerHTML = ("Published Date: "+"March"+" "+date+" "+year).bold();
				}
				else if(month == "02"){
					cellPubdate.innerHTML = ("Published Date: "+"February"+" "+date+" "+year).bold();
				}
				else if(month == "01"){
					cellPubdate.innerHTML = ("Published Date: "+"January"+" "+date+" "+year).bold();
				}
				
		}
		
		
		//SUMMARY
		if(results.volumeInfo.description === undefined ||
				results.volumeInfo.description == null)
			{
				cellSummary.innerHTML = "No Summary Available!";
			}
		else{
			cellSummary.innerHTML = ("Summary: ").bold()+results.volumeInfo.description;
			
		}
		
		//ISBN		
		if(results.volumeInfo.industryIdentifiers == null){
			cellIsbn.innerHTML = ("ISBN is not available!");
		}
		else if(results.volumeInfo.industryIdentifiers.length == 2){
			
			cellIsbn.innerHTML = ("ISBN: "+results.volumeInfo.industryIdentifiers[1].identifier).bold();
		}
		else if(results.volumeInfo.industryIdentifiers.length == 1){
			
			cellIsbn.innerHTML = ("ISBN: "+results.volumeInfo.industryIdentifiers[0].identifier).bold();
		}
		
		
	
		//APPENDCHILD all ITEM to TABLE
		cellContent.appendChild(cellTitle);
		cellContent.appendChild(cellAuthor);
		cellContent.appendChild(cellPublisher);
		cellContent.appendChild(cellPubdate);
		cellContent.appendChild(cellIsbn);
		cellContent.appendChild(cellSummary);
		cellContent.appendChild(cellremove);

		
		trElement.appendChild(cellContent);
		tbodyElement.appendChild(trElement);
	}
	
	
}

</script>
</body>
</html>