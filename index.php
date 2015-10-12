<!DOCTYPE html>
<html>
	<head>
		<?php	
			$pageTitle = "Login";
			include "head.php";
		?>	
	</head>
	<body>
		<h1 class="col-xs-offset-4_5 vert-offset-top-2">Mischief Manager</h1>
		<form method="post" action="loginOnServer.php" class="col-xs-3 col-xs-offset-4">
			<h3>Login</h3>
			<label class="text-left" for="nickname">Evil Nickname</label>
			<br/>
			<input class="form-control" type="text" id="nickname" name="nickname" required/>
			<br/>
			<label for"password">Password</label>
			<br/>
			<input class="form-control" type="password" id="password" name="password" required/>
			<br/>
			<br/>
			<input class="btn-sm btn-info col-xs-offset-5" type="submit" value="Login" id="login"/>
			<!--br/>
			<label id="register">Register here</label-->
		</form>
	</body>
</html>