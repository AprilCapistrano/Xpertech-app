<?php  
 require "init.php";  
 $remote_detail_id = $_POST["remote_detail_id"];
 $sql_query = "SELECT remote_desc FROM remote_detail WHERE remote_detail_id = '$remote_detail_id';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['remote_desc'];
	 	$index++;
	 }
	 $selfinstall_title = implode("$", $array);
	 echo $selfinstall_title;
 }  
 else  
 {   
 echo "No cable box found.";  
 }  
 ?>  