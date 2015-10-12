<?php
	include "head.php";

	$nickname = $_POST['nickname'];
	$password = $_POST['password'];

	$result = mysqli_query($con, "SELECT * FROM Users WHERE BINARY EvilNickname = BINARY '" . $nickname . "' AND BINARY Password = BINARY '" . $password . "'"); 

 	if(mysqli_num_rows($result) > 0){
		$_SESSION['loggedIn'] = true;
		$_SESSION['nickname'] = $nickname;
 		?>
	 		<script type='text/javascript'>
				window.location = "calendar.php";
			</script>
		<?php
	}
 	else
 	{
 		?>
 		<script type='text/javascript'>
			alert("Login failed. Please try again.");
			window.location = "index.php";
		</script>
		<?php
 	}

 	include"menu.php";
?>