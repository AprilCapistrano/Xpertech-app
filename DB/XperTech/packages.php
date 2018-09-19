<?php  
 require "init.php";  
 $sql_query = "SELECT package_name FROM packages ";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['package_name'];
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