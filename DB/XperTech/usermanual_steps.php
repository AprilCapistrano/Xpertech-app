<?php  
 require "init.php";  
 $position = $_POST["position"];
 $box_id = $_POST["box_id"];
 $sql_query = "SELECT manual_desc from manual_step where position = '$position' and box_id = '$box_id';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['manual_desc'];
	 	$index++;
	 }
	 $manual_steps_desc = implode("$", $array);
	 echo $manual_steps_desc;
 }  
 else  
 {   
 echo "No cable box found.";  
 }  
 ?>  