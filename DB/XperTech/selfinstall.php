<?php  
 require "init.php";  
 $box_number = $_POST["box_number"];
 $sql_query = "select selfinstall_title from selfinstall where box_number = '$box_number';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['selfinstall_title'];
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