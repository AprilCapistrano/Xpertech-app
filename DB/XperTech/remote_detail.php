<?php  
 require "init.php";  
 $sql_query = "select remote_detail_name from remote_detail;";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['remote_detail_name'];
	 	$index++;
	 }
	 $selfinstall_title = implode("$", $array);
	 echo $selfinstall_title;
 }  
 else  
 {   
 echo "No list found";  
 }  
 ?>  