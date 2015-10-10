<?php
	
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}
	
	
	if (isset($_GET["username"]))
	{
		$user = $_GET["username"];
	}
	
	
	$result = mysqli_query($con, "SELECT * FROM ToDo where username = $user ORDER BY date DESC, time DESC");
	
	

	$item = array();
	
	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result))
		{
			//username, entry, date, time
			 $item["username"] = $row["username"];
			 $item["entry"] = $row["entry"];
			 $item["date"] = $row["date"];
			 $item["time"] = $row["time"];
	 
		}
	}
	
	
	
	mysqli_free_result($result);	 
	 mysqli_close($con);
	 

	echo json_encode($item); 
?>