<?php  
 require "init.php";  
 $box_number = $_POST["box_number"];
 $sql_query = "select troubleshoot_title from troubleshoot where box_number = '$box_number';";  
 $result = mysqli_query($con,$sql_query); 
 $array = array();
 $index = 0;
 if(mysqli_num_rows($result) > 0 ) {  
	 while ($row = mysqli_fetch_assoc($result)) {
	 	$array[$index] = $row['troubleshoot_title'];
	 	$index++;
	 }
	 $troubleshoot_title = implode("$", $array);
	 echo $troubleshoot_title;
 }  
 else  
 {   
 echo "No cable box found.";  
 }  
 ?>  