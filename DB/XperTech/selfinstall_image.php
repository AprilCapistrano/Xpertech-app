<?php  
 require "init.php";  
 $selfinstall_id = $_POST["selfinstall_id"];
 $box_id = $_POST["box_id"];
 $sql_query = "select install_image_id from selfinstall_steps where selfinstall_id = '$selfinstall_id' AND box_id = '$box_id';";   
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['install_image_id'];
	 	$index++;
	 }
	 $install_steps_desc = implode("$", $array);
	 echo $install_steps_desc;
 }  
 else  
 {   
 echo "No cable box found.";  
 }  
 ?>  