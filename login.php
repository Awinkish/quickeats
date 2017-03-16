<?php


error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

$response = array("error" => FALSE);

if (isset($_POST['fname']) && isset($_POST['sname']) && isset($_POST['email']) && isset($_POST['referral']) && isset($_POST['mobile']) && isset($_POST['password'])) {

    // receiving the post params
    $fname = $_POST['fname'];
    $sname = $_POST['sname'];
    $referral = $_POST['referral'];
    $mobile = $_POST['mobile'];
    $email = $_POST['email'];
    $password = $_POST['password'];


    // give the new user a referral code

    $refferalcode = substr(sha1(rand(100 * gettimeofday()['sec'], 500)), 0, 10);

    // check if user is already existed with the same email
    if ($db->isUserExisted($email,'users')) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    }else if ($db->isNumberExisted($mobile,'users')) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $mobile;
        echo json_encode($response);
    }  else {

        if ($referral != "NULL") {

        $reff = $db->check_who_reffered("users", "refferal_code", $referral);

            if ($reff == false) {
               //echo 'No user with given refferal code exist <br>';
            } else {

               $userid = $reff["userid"];

               // create a new user
               $user = $db->storeUser($fname, $sname, $refferalcode, $mobile, $email, $password, $userid);
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

            $reff = "";
    
        }
        
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>
