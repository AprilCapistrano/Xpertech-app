<?php  
 require "init.php";  
 $status = $_POST["status"];
 $type = $_POST["type"];
 $sql_query = "SELECT COUNT(stat_id) AS result FROM stat_info WHERE status = '$status' AND type = '$type';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['result'];
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