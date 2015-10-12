<?php
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(isset ($_GET["username"]) && isset ($_GET["entry"]) && isset ($_GET["date"]) && isset ($_GET["time"]))
	{
		$user = $_GET["username"];
		$oldEntry = $_GET["oldEntry"];
		$entry = $_GET["entry"];
		$date = $_GET["date"];
		$time = $_GET["time"];
	}
		
	$json = array();

	mysqli_query($con, "UPDATE ToDo SET username = '$user', entry = '$entry', date = '$date', time = '$time' WHERE entry = '$oldEntry' AND username = '$user'");
	
	mysqli_close($con);

?>