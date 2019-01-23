<?php 
 
require_once '../includes/DbOperations.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    if(isset($_POST['username']) and isset($_POST['password']) and isset($_POST['category']) ){
        $db = new DbOperations();
        
        if($db->userLogin($_POST['username'],$_POST['password'],$_POST['category'])){
            
            $user = $db->getUserByUsername($_POST['username'], $_POST['category']);
            
            $response['error'] = false;
            $response['id'] = $user['id'];
            $response['email'] = $user['email'];
            $response['username'] = $user['username'];
            $response['category'] = $user['category'];
        }else {
            $response['error'] = true; 
            $response['message'] = "Invalid username or password or catagory";
        }
        
    }else {
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
    
    echo json_encode($response);
    
    
}

?>