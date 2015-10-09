<?php
	
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}
	
	if(isset ($_POST["username"]) && isset ($_POST["entry"]) && isset ($_POST["date"]) && isset ($_POST["time"]))
	{
		$user = $_POST["username"];
		$entry = $_POST["entry"];
		$date = $_POST["date"];
		$time = $_POST["time"];
		
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