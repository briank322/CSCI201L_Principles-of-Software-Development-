<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>SearchResult</title>

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
  padding-left:70px;	
  padding-bottom: 15px;
  padding-top: 15px;
  text-align: left;
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
font-size: 36px;

}

.table th, .table td {
	text-align: center;
	vertical-align: middle;
}

.row{
padding-left: 10%;
padding-right:10%;
}
}

</style>
<body>

<div class = "header">
<a href='HomePage.jsp'>
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
</div>
</div>


<hr id ="headline"/>
<div class = "row">

<div id = "firstline">
Results for "<%=request.getAttribute("keyword")%>"
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
	
	
	/* if((optionBox1.checked)||(optionBox2.checked)||(optionBox3.checked)||(optionBox4.checked)){
		
		return true;
	}
	else{
		
		alert("Option Box is empty");
		return false;
	}  */
	else{
		return true;
	}
	
}

let endpoint = "";

<%-- if(<%=request.getAttribute("option")%> == null || <%=request.getAttribute("option")%> ==""){
	"https://www.googleapis.com/books/v1/volumes?q="+ "<%=request.getAttribute("keyword")%>"+
	"&key=AIzaSyAJ-cUh9fsVoTRGyPV437WB1aTNHuMmeNo";
}  --%>
	
	endpoint = "https://www.googleapis.com/books/v1/volumes?q="
		+"<%=request.getAttribute("option")%>"
		+":<%=request.getAttribute("keyword")%>"
		+"&key=AIzaSyAJ-cUh9fsVoTRGyPV437WB1aTNHuMmeNo";

		
		
//GETTING a JSON and making an AJAX Function, I referenced the template code from 
//my current ITP class coursework and Professor's source code which is open to 
//All ITP 303 students. The github link for this code is the following
//https://github.com/nayeonk/itp303-am-fall-2019/tree/master/lect10-api
	


function ajax(endpoint) {

	let xhr = new XMLHttpRequest();
	xhr.open("GET", endpoint);
	xhr.send();
	
	xhr.onreadystatechange = function() {
		console.log(this);
		if(this.readyState == this.DONE) {
			if(xhr.status == 200) {
				console.log( xhr.responseText);
				console.log( JSON.parse(xhr.responseText))
				let bookResults = JSON.parse( xhr.responseText );
				console.log("TotalItems: "+bookResults.totalItems);
				if(bookResults.totalItems > 0){
					displayResults(bookResults);
				}
				
				else {
				console.log("Error in AJAX!");
				console.log(xhr.status);
				window.confirm("No Search Results!");
				window.location.replace("HomePage.jsp");
				}

		}

	}

}
}
ajax(endpoint);
function displayResults(results){
	 
	let tbodyElement = document.querySelector("tbody");

	while( tbodyElement.hasChildNodes()){
		tbodyElement.removeChild(tbodyElement.lastChild);
	}

	for( let i = 0; i < results.items.length; i++){
		
		let trElement = document.createElement("tr");
		//trElement.class = "border";
		let nailimage = document.createElement("a");
	 	nailimage.href = "Details.jsp";
	 	nailimage.onclick = function(){
	 		sessionStorage.setItem("bookdetail",results.items[i].selfLink);
	 		
	 	};
	 
		let cellContent = document.createElement("td");
		let cellThumbnail = document.createElement("td");
		let cellTitle = document.createElement("h2");
		let cellAuthor = document.createElement("h3");
		let cellSummary = document.createElement("p");
		
		//Create an <img> tag for cover image
		let imgElement = document.createElement("img");
		if(results.items[i].volumeInfo.imageLinks == null)
		{
			imgElement.src= "image/nimg2.png";	
		}
		else{
			imgElement.src = results.items[i].volumeInfo.imageLinks.thumbnail;
		}
		
		//Append the <img> tag to its <td> tag
		nailimage.appendChild(imgElement);
		cellThumbnail.appendChild(nailimage);
		trElement.appendChild(cellThumbnail);
		
	
		//Add text to artist, track, and album cells
		if(results.items[i].volumeInfo.title == null){
			cellTitle.innerHTML = "No Title Available!";
		}
		else{
			cellTitle.innerHTML = results.items[i].volumeInfo.title;
		}
		
		if(results.items[i].volumeInfo.authors == null){
			cellAuthor.innerHTML = "No Author Available!";
		}
		else{
			cellAuthor.innerHTML = results.items[i].volumeInfo.authors;

		}
		
		
		//SUMMARY 
		if(results.items[i].searchInfo== null){
			if(results.items[i].volumeInfo.description === undefined ||
					results.items[i].volumeInfo.description == null)
				{
					cellSummary.innerHTML = "No Summary Available!";
				}
			else{
				cellSummary.innerHTML = ("Summary: ").bold()+results.items[i].volumeInfo.description;
				
			}
		}
		else{
			cellSummary.innerHTML = ("Summary: ").bold()+results.items[i].searchInfo.textSnippet;
		}
		

		cellContent.appendChild(cellTitle);
		cellContent.appendChild(cellAuthor);
		cellContent.appendChild(cellSummary);
		
		trElement.appendChild(cellContent);
		tbodyElement.appendChild(trElement);
		
	
		
	}

	
	
}

</script>
</body>
</html>