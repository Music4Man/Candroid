<?php
	
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}
	
	if(isset ($_GET["username"]) && isset ($_GET["entry"]) && isset ($_GET["date"]) && isset ($_GET["time"]))
	{
		$user = $_GET["username"];
		$entry = $_GET["entry"];
		$date = $_GET["date"];
		$time = $_GET["time"];
		
		mysqli_query($con, "INSERT INTO ToDo (username, entry, date, time) VALUES ('$user', '$entry', '$date', '$time')");

		$json = array();
	
		mysqli_close($con);

		echo json_encode($json); 
	}
	else
	{
		mysqli_close($con);
	}

?>