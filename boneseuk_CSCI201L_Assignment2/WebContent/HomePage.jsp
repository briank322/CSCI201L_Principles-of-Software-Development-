<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HomePage</title>
</head>
<style>
body{
margin:0;
padding:0;
}
#bookworm img{
height: 150px;
    padding-top: 20px;
    padding-left: 20px;
}

.background{
  height: 800px;
  background-image: url("image/background.jpg");
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  position: relative;

}
.background-text{
width: 80%;
font-family: "Arial";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
}

.form-control{
width: 100%;
height: 25px;
font-size: 14px;
}

.btn {
  border: 2px solid gray;
  background-color: black;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  float: right;
}

#radioleft{
float: left;
width:50%;
}

#radioright{
float: left;
width:50%;
}

.radios{
padding-bottom: 4px;

}

</style>
<body>

<div id="bookworm">
<img src="image/bookworm.png"/>
</div>
<div class="background">
  <div class="background-text">
    <h1 style="font-size:45px">BookWorm: Just a Mini Program... Happy Days!</h1>
    
    <form name = "myform" method =
     "GET" action="Validation">
    <input type="text" name="keyword" class="form-control" id="search-id" placeholder="Search for your favorite book!">
<%=request.getAttribute("error") == null? "":request.getAttribute("error")%> 

<br> 
<br>
<div id="radioleft">
<div class="radios">
<input type = "radio" id="name"
class="line1" name = "option" value = ""> Name   
</div>      
<div class = "radios">
<input type = "radio" id="author" class="line2" name = "option" value = "inauthor"> Author
</div>
</div>


<div id="radioright">
<div class="radios">
<input type = "radio" id="isbn" class="line1" name = "option" value = "isbn"> ISBN 
</div>      
<div class = "radios">
<input type = "radio" id="publisher" class="line2" name = "option" value = "inpublisher"> Publisher
</div>
</div>


<button type="submit" class="btn default">Search!</button>

<%=request.getAttribute("error2") == null? "":request.getAttribute("error2")%> 



</form>
</div>
</div>

<script>
var text;
var radio;
function search(){
	if(document.getElementById("name").checked){
		radio = "name";
		console.log("name clicked!");
	}
	else if(document.getElementById("author").checked){
		radio = "author";
		console.log("author clicked!");
		
	}
	else if(document.getElementById("isbn").checked){
		radio = "isbn";
		console.log("isbn clicked!");
		
	}
	else if(document.getElementById("publisher").checked){
		radio = "publisher";
		console.log("publisher clicked!");
		
	}
	text = document.getElementById("search-id").value;
	console.log(text);
	
}


</script>
</body>
</html>