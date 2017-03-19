<?php


error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'include/DB_Functions.php';
$db = new DB_Functions();
//$person = new referral();

$response = array("error" => FALSE);

if (isset($_POST['uid']) && isset($_POST['address']) && isset($_POST['nickname']) && isset($_POST['description']) ) {

    // receiving the post params
    $uid = $_POST['uid'];
    $address = $_POST['address'];
    $nickname = $_POST['nickname'];
    $description = $_POST['description'];
   
    $user = $db->storeAddress($uid, $address, $nickname, $description);

    if ($user) {
        
        // user stored successfully
        $response["error"] = FALSE;
        $response["uid"] = $user["userid"];
        $response["user"]["fname"] = $user["formatted_address"];
        $response["user"]["sname"] = $user["nickname"];
        $response["user"]["success"] = "Address added";
        echo json_encode($response);
    } else {
        
        // user failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in registration!";
        echo json_encode($response);
    }

                
   
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}

?>






