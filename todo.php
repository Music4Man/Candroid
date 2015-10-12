<!DOCTYPE html>
<html>
	<head>
		<?php	
			$pageTitle = "To Do List";
			include "head.php";
		?>	
	</head>
	<body>
		<?php include "menu.php"; ?>
		<h1 class="col-xs-offset-4 vert-offset-top-2">Mischief Manager</h1>
		<h2>Todo</h2>
		
		
		
		<section>
			<div id="products">
				<?php
				//current URL of the Page.
				$current_url = base64_encode("http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);
				
				$nickname = $_SESSION['nickname'];
				    
				    $result = mysqli_query($con, "SELECT * FROM ToDo where username = '$nickname' ORDER BY date DESC, time DESC");
	
	

					//$item = array();
					
					if(mysqli_num_rows($result) > 0){
						while($row = mysqli_fetch_assoc($result))
						{
							
							 echo '<div class="item">'; 
							  echo '<div class="item_todo"><h4 name="h">'.$row["entry"].'</h4>';
							    echo '<div class="item_date">Complete by: '.$row["date"].'</div>';
							    echo '<br><div class="item_time">At: '.$row["time"].'</div>';
							    echo '</div><hr>';
						}
					}
					
	
	
					mysqli_free_result($result);	 
					 mysqli_close($con);
					
				    
				
				?>
			</div>
		</section>
	</body>
</html>