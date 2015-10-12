<?php
    if(!isset($pageTitle)) {
        $pageTitle = "Mischief Manager";
    }
?>

		
		<title><?php echo($pageTitle) ?></title>
		<link rel="stylesheet" type="text/css" href="style.css">
		<!--media="screen and (-webkit-min-device-pixel-ratio: 0)"-->
		<link rel="stylesheet" type="text/css" href="bootstrap.css">
		<!--script src="script/jquery-1.11.2.min.js"></script-->
		<!--script src="script/script.js" type="text/javascript"></script-->
		<meta name="viewport" content="width=device-width, initial-scale=1"/>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<?php require_once('connection.php'); ?>
