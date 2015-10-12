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
	
	$result = mysqli_query($con, "SELECT * FROM ToDo WHERE entry = '$entry' AND username = '$user'");
	
	$item = array();
	
	if(mysqli_num_rows($result) > 0)
	{
		$row = mysqli_fetch_assoc($result);
		
			$item = array();
			//username, entry, date, time
			 $item["username"] = $row["username"];
			 $item["entry"] = $row["entry"];
			 $item["date"] = $row["date"];
			 $item["time"] = $row["time"];
	 
		$response["item"] = $item;
	}
	
	
	
	mysqli_free_result($result);	 
	 mysqli_close($con);
	 
	 echo json_encode($response);
	
?>