<?php

	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

	if(mysqli_connect_errno()){
		echo "Failed to connect: ".mysqli_connect_error();
	}

	//Values gotten from the App
	if(isset ($_POST["name"])){
		$name = $_POST["name"];
	}

	if(isset ($_POST["surname"])){
		$surname = $_POST["surname"];
	}

	if(isset ($_POST["age"])){
		$age = $_POST["age"];
	}

	if(isset ($_POST["nickname"])){
		$nickname = $_POST["nickname"];
	}

	if(isset ($_POST["email"])){
		$email = $_POST["email"];
	}

	if(isset ($_POST["password"])){
		$password = $_POST["password"];
	}

	mysqli_query($con, "INSERT INTO Users (Name, Surname, Age, EvilNickname, Email, Password) VALUES ('".$name."', '".$surname."', '".$age."', '".$nickname."', '".$email."', '".$password."')");

	$json = array();

	if (mysqli_affected_rows($con) != 0) {
		$result = mysqli_query($con, "SELECT * FROM Users WHERE BINARY EvilNickname = BINARY '" . $nickname . "' AND BINARY Password = BINARY '" . $password . "'");
	 
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
	}
	
	mysqli_close($con);

	echo json_encode($json); 
?>