<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Login</title>
<script
  src="http://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>
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

#loginform{
padding-left: 37%;
}

#username, #password{
	width: 400px;
	height: 40px;
	font-size: 30px;
}
#submitbutton{
	width: 400px;
	height: 40px;

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
<hr>
<form name = "loginform" method ="POST" action="Loggedinhomepage.jsp" onsubmit= "return success()" >
	<div id = "loginform">
	<h3> Username</h3>
	<input id ="username" type="text" name="username" >
	<br>
	<span style = "color: red;"> ${unError}</span>
	<span id = "userexists" style = "color: red;"> ${userexists}</span>
	<%=request.getAttribute("nouser") == null? "":request.getAttribute("nouser")%> 
	<h3> Password</h3>
	<input id ="password" type="password" name="password" >
	<br>
	<span style = "color: red;"> ${pwError}</span>
	<%=request.getAttribute("nopw") == null? "":request.getAttribute("nopw")%> 
	
	<br>
	<button id = "submitbutton" style = "font-size: 20px;"type="submit">Sign In</button>
	</div>

</form>



<script>
function success() {

	var username = document.getElementById('username').value;
	var password = document.getElementById('password').value;
	//var confirmpassword = document.getElementById("confirmpassword").value;
	var error;
	
	$.ajax({
		async: false,
		url: 'LoginValidate',
		data: {username: username,
			   password: password
			   },
		type: 'get',
		cache: false,
		success: function(data)
		{
			error = data;
			if(data.length>0) {
				window.alert(data);
			}
			else {
				window.alert("Successful login!");			
			}
		}
			
	});
	console.log(error.length);
	if(error.length < 1) {
		document.cookie = "username="+document.loginform.username.value+";";
		return true; 
	} 
	else
		return false;
	
}


</script>

</body>
</html>