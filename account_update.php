<?php


error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'include/DB_Functions.php';
$db = new DB_Functions();
//$person = new referral();

$response = array("error" => FALSE);

if (isset($_POST['fname']) && isset($_POST['sname']) && isset($_POST['email']) && isset($_POST['mobile']) && isset($_POST['password'])) {

    // receiving the post params
    $fname = $_POST['fname'];
    $sname = $_POST['sname'];
    $mobile = $_POST['mobile'];
    $email = $_POST['email'];
    $password = $_POST['password'];


    // check if user is already existed with the same email
    if ($db->isUserExisted($email,'users')) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already exists with " . $email;
        echo json_encode($response);
    }else if ($db->isNumberExisted($mobile,'users')) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already exists with " . $mobile;
        echo json_encode($response);
    } else {

            if ($referral != "NULL") {

                $reff = $db->check_who_reffered($referral);

                $userid = $reff["userid"]; 

                $user = $db->updateEntity($fname, $sname, $refferalcode, $mobile, $email, $password, $userid);

                if ($reff == false) {
                   //echo 'No user with given refferal code exist <br>';
                } else {

                }

            } else {

                $userid = 0; 

                $user = $db->updateEntity($fname, $sname, $refferalcode, $mobile, $email, $password, $userid);
        
            }
            
            if ($user) {
                // user stored successfully
                $response["error"] = FALSE;
                $response["uid"] = $user["unique_id"];
                $response["user"]["fname"] = $user["firstname"];
                $response["user"]["sname"] = $user["lastname"];
                $response["user"]["mobile"] = $user["phoneno"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                    // user failed to store
                $response["error"] = TRUE;
                $response["error_msg"] = "Unknown error occurred in registration!";
                echo json_encode($response);
            }
            
        
        }
   
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}

?>






