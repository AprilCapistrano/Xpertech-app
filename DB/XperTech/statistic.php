 <?php  
 require "init.php";  
 $type = $_POST["type"];  
 $status = $_POST["status"];
 $ownership = $_POST["ownership"];
 $sql_query = "INSERT INTO stat_info(type,status,ownership) values('$type','$status','$ownership');";

 mysqli_query($con,$sql_query);  

 echo "Success";
 ?> 