<?php
	
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}


	$response = array();
	
	if (isset($_POST["entry"]))
	{
		$entry = $_POST["entry"];
	}
	
	$result = mysqli_query($con, "DELETE FROM products WHERE entry = $entry");
	

?>