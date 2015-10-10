<!DOCTYPE html>
<html>

	<head>
		<?php
			//$con = mysqli_connect("localhost", "Candroid", "kmTYHA6q", "Candroid");
			$con = mysqli_connect("localhost", "root", "", "Candroid");

			if(mysqli_connect_errno())
			{
				echo "Failed to connect: ".mysqli_connect_error();
			}
		?>
		<title>ToDoList</title>
	</head>

	<body>
		
		<header>
			<h1>To Do List</h1>
		</header>
		
		<section>
			<div id="products">
				<?php
				//current URL of the Page.
				$current_url = base64_encode("http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);
				    
				    $result = mysqli_query($con, "SELECT * FROM ToDo where username = 'FireStorm' ORDER BY date DESC, time DESC");
	
	

					//$item = array();
					
					if(mysqli_num_rows($result) > 0){
						while($row = mysqli_fetch_assoc($result))
						{
							
							 echo '<div class="item">'; 
							  echo '<div class="item_todo"><h3 name="h">'.$row["entry"].'</h3>';
							    echo '<div class="item_date">Complete by: '.$row["date"].'</div>';
							    echo '<br><div class="item_time">At: '.$row["time"].'</div>';
							    echo '</div>';
						}
					}
					
	
	
					mysqli_free_result($result);	 
					 mysqli_close($con);
					
				    
				
				?>
			</div>
		</section>
	</body>
</html>