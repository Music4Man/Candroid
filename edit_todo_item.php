<?php
	
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}
	
	
	if (isset($_POST["entry"]))
	{
		$entry = $_POST["entry"];
	}
	
	$result = mysqli_query($con, "SELECT * FROM products WHERE entry = $entry");
	
	$item = array();
	
	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result))
		{
			//username, entry, date, time
			 $item["username"] = $result["username"];
			 $item["entry"] = $result["entry"];
			 $item["date"] = $result["date"];
			 $item["time"] = $result["time"];
	 
		}
	}
	
	
	
	mysqli_free_result($result);	 
	 mysqli_close($con);
	 
	 echo json_encode($item);
	
?>