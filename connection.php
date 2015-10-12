<?php
	$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");
	session_start();

	if(!isset($_SESSION['loggedIn']))
		$_SESSION['loggedIn'] = false;
?>