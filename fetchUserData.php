<?php
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(isset ($_POST["password"])){
		$password = $_POST["password"];
	}

	if(isset($_POST["nickname"])){
		$nickname = $_POST["nickname"];
	}

	$result = mysqli_query($con, "SELECT * FROM Users WHERE BINARY EvilNickname = BINARY '" . $nickname . "' AND BINARY Password = BINARY '" . $password . "'");
	
	$json = array();
 
	if(mysqli_num_rows($result) > 0){
		while($row = mysqli_fetch_assoc($result)){
			$json['nickname'] = $row["EvilNickname"];
			$json['password'] = $row["Password"];
			$json['age'] = $row["Age"];
			$json['email'] = $row["Email"];
			$json['surname'] = $row["Surname"];
			$json['name'] = $row["Name"];
		}
	}
	
	mysqli_free_result($result);
	mysqli_close($con);
	
	echo json_encode($json); 
?>