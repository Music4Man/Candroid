<?php
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");

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

	if(isset ($_POST["originalNickName"])){
		$originalNickName = $_POST["originalNickName"];
	}

	if(isset ($_POST["oldPassword"])){
		$oldPassword = $_POST["oldPassword"];
	}

	$json = array();

	mysqli_query($con, "UPDATE Users SET Name = '$name', Surname = '$surname', Age = '$age', EvilNickname = '$nickname', Email = '$email', Password = '$password' WHERE EvilNickname = '$originalNickName' AND Password = '$oldPassword'");
	
	if (mysqli_affected_rows($con) != 0) 
	{
		mysqli_query($con, "UPDATE ToDo SET username = '$nickname' WHERE username = '$originalNickName'");


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