<?php  
 require "init.php";  
 $package_id = $_POST["package_id"];
 $sql_query = "SELECT channel_name FROM channels WHERE package_id = '$package_id';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['channel_name'];
	 	$index++;
	 }
	 $package_title = implode("$", $array);
	 echo $package_title;
 }  
 else  
 {   
 echo "No cable box found.";  
 }  
 ?>  