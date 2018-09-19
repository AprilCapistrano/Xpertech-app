<?php  
 require "init.php";  
 $accountNumber = $_POST["accountNumber"];
 $sql_query = "select subscriber_id, box_number from ownership where ownership_id = '$accountNumber';";  
 $result = mysqli_query($con,$sql_query);  
 if(mysqli_num_rows($result) >0 )  
 {  
 $row = mysqli_fetch_assoc($result);  
 $subscriber_id =$row["subscriber_id"];  
 $box_number =$row["box_number"];  
 echo $box_number.",".$subscriber_id;  
 }  
 else  
 {   
 echo "Invalid QR code. Please try again.";  
 }  
 ?>  