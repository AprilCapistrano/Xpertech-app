<?php  
 require "init.php";  
 $troubleshoot_id = $_POST["troubleshoot_id"];
 $box_id = $_POST["box_id"];
 $sql_query = "SELECT trbl_steps_desc FROM troubleshoot_steps WHERE troubleshoot_id = '$troubleshoot_id' AND box_id = '$box_id';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['trbl_steps_desc'];
	 	$index++;
	 }
	 $trbl_steps_desc = implode("$", $array);
	 echo $trbl_steps_desc;
 }  
 else  
 {   
 echo "No cable box found.";  
 }  
 ?>  