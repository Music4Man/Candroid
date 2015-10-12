<?php
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(isset($_POST["user"])){
		$user = $_POST["user"];
	}
	if(isset($_POST["date"])){
		$date = $_POST["date"];
	}

	$result = mysqli_query($con, "SELECT * FROM ToDo WHERE BINARY username = BINARY '" . $user . "' AND BINARY date = BINARY '" . $date . "'");
	
	$json = array();
 
	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result)){
			$json['username'] = $row["username"];
			$json['entry'] = $row["entry"];
			$json['date'] = $row["date"];
			$json['time'] = $row["time"];
		}
	}
	
	mysqli_free_result($result);
	mysqli_close($con);
	
	echo json_encode($json); 
?>