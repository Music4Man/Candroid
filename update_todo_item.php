<?php
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(isset ($_POST["username"]) && isset ($_POST["entry"]) && isset ($_POST["date"]) && isset ($_POST["time"]))
	{
		$user = $_POST["username"];
		$oldEntry = $_POST["oldEntry"];
		$entry = $_POST["entry"];
		$date = $_POST["date"];
		$time = $_POST["time"];
	}
		
	$json = array();

	mysqli_query($con, "UPDATE ToDo SET username = '$user', entry = '$entry', date = '$date', time = '$time' WHERE entry = '$oldEntry'");
	
	mysqli_close($con);

?>