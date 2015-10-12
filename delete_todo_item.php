<?php
	
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}


	$response = array();
	
	if (isset($_GET["entry"]) && isset($_GET["username"]))
	{
		$entry = $_GET["entry"];
		$user = $_GET["username"];
	}
	
	$result = mysqli_query($con, "DELETE FROM ToDo WHERE entry = '$entry' and username='$user'");
	
	mysqli_close($con);
?>