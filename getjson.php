<?php
// $host="localhost"; //replace with database hostname 
// $username="root"; //replace with database username 
// $password=""; //replace with database password 
// $db_name="hotels"; //replace with database name
require_once '../includes/Constants.php';

$con=mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD ) or die ("cannot connect to database"); 
mysqli_select_db($con, DB_NAME) or die ("cannot select DB");

$sql = "select bloodgroup from registration"; 
 
$result = mysqli_query($con,$sql);
$json = array();
 
 
if(mysqli_num_rows($result)){
while($row=mysqli_fetch_assoc($result)){
$json['registration'][]=$row;
 
}
 
}
 
mysqli_close($con);
echo json_encode($json); 
?>