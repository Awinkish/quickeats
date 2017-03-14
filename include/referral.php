<?php

/**This Code is called if the post parameter that came in had among them a post variable 'userregister'
*There are several methods ... 
* the sanitize method, copied here too //Input Sanitizer
* the check_who_reffered method called from user_registration, copied  // Very important, it checks the id of the user who owns the refferal code used by a user during registration
*  the  add_to_db called from user_registration,     // This pushes the data to the insert function 
* the insert function normally on another class but assume its in the same file(class) for simplicity// this inserts the data to db
* Anywhere u see global $connector, thats my connection variable
**/



  public function insert($table, $columns, $values,$keycolumn, $keyvalue)
    {
        global $connector;
        $done = false;
        $sql = "INSERT INTO " . $table . " ( " . $columns . " ) VALUES (" .$values . ")";
        //echo $sql;

        $sqlcheck = "SELECT * FROM " . $table . " WHERE " . $keycolumn . " = '" . $keyvalue . "'";

        $resultcheck = mysqli_query($connector, $sqlcheck);

        if (!$resultcheck) {
            //  echo $sqlcheck;
            // echo "The File for that account exists";
            echo mysqli_error($connector);
            $done = false;
        } else{
            //echo 'Not exist';
            $countcheck = mysqli_num_rows($resultcheck);
            if ($countcheck > 0) {
                 echo "Existing Record for similar key value   --   ";
                $done = false;
            }else{
                if (!mysqli_query($connector, $sql)) {
                    $done = false;
                    echo mysqli_error($connector);
                } else {
                    $done = true;
                }
            }
        }


        return $done;
    }






 function add_to_db($columns, $values, $table, $keycolumn, $keyvalue)
    {
        global $dh;
        $message = '';
        // account_name,claim_no,description, start_date
        if ($dh->insert($table, $columns, $values, $keycolumn, $keyvalue)) {

            $_SESSION['message'] = "Successfully Registered";
            $_SESSION['messagetype'] = "Success";
            $message = "Success";
        } else {

            //  echo "User could not be added";
            $_SESSION['message'] = "User could not be added";
            $_SESSION['messagetype'] = "Error";
            $message = "error";
        }
        return $message;
        header("Location: ../");
    } 

function sanitize($input){

    $output = html_entity_decode(addslashes($input));
    return $output;
}

    function check_who_reffered($table, $checkcolumn, $checkvalue)
    { //Function to Check who reffered the User to Register
        global $connector;
        $id = 0;
        $sqlcheck = "SELECT * FROM " . $table . " WHERE " . $checkcolumn . " = '" . $checkvalue . "'";

        $resultcheck = mysqli_query($connector, $sqlcheck);

        if (!$resultcheck) {
            //  echo $sqlcheck;
            // echo "The File for that account exists";
            echo mysqli_error($connector);

        } else {
            //echo 'Not exist';
            $countcheck = mysqli_num_rows($resultcheck);
            if ($countcheck > 0) {

                $data = mysqli_fetch_array($resultcheck);
                $id = $data['userid'];

            } else {
                $id = 0;
            }
        }

        return $id;
    }


if (isset($_POST['userregister'])) {
    $table = "users";
    $keycolumn = "email"; //Column which no other user should have such a value
    if (isset($_POST['refferal'])) {
        $refferer = sanitize($_POST['refferal']);
        if ($_POST['refferal'] == "NULL") {
          //echo "empty referal code <br>";
            $reff = "";
        } else {
            $reff = $person->check_who_reffered("users", "refferal_code", $refferer);
            if ($reff == 0) {
               // echo 'No user with given refferal code exist <br>';
            } else {
             //   echo "You were reffered by the User with ID " . $reff . "<br>";

            }
        }
    } else {
        $reff = "";
    }

    $fname = sanitize($_POST['fname']);
    $lname = sanitize($_POST['lname']);
    //  $refferal = sanitize($_POST['refferal']);
    $email = sanitize($_POST['email']);
    $phoneno = sanitize($_POST['phoneno']);
    if ($dh->check_duplicates('phoneno', $phoneno, $table)) {
        $_SESSION['message'] = "Phone Number Already Used";
        $_SESSION['messagetype'] = "Error";
    } else {

        $password = md5(sanitize($_POST['password']));
        $cfmpassword = md5(sanitize($_POST['cfmpassword']));

        if ($password != $cfmpassword) {
            $_SESSION['message'] = "Passwords Entered did not Match";
            $_SESSION['messagetype'] = "Error";
            header("Location: ../");
        } else {


            $formattedaddress = sanitize($_POST['formattedaddress']);
            $add_lat = sanitize($_POST['addresslat']);
            $add_long = sanitize($_POST['addresslong']);
            $refferalcode = substr(sha1(rand(100 * gettimeofday()['sec'], 500)), 0, 10);
            $keyvalue = $email; //value , unique to that user, not necessaryly primary key
            $acc_status = "Active";
            //  firstname,  lastname ,  email , password ,  address_lat , address_lng,  formatted_address , phoneno , account_status,   refferal_code,  discount
            $columns = "firstname,  lastname ,  email , password ,  address_lat , address_lng,  formatted_address , phoneno , account_status,   refferal_code, reffered_by";
            //echo $columns;
            //separate -            . "','".
            $values = "'" . $fname . "','" . $lname . "','" . $email . "','" . $password . "','" . $add_lat . "','" . $add_long . "','" . $formattedaddress . "','" . $phoneno . "','" . $acc_status . "','" . $refferalcode . "','" . $reff . "'";
       $message =     $person->add_to_db($columns, $values, $table, $keycolumn, $keyvalue);
        }
    }
    header("Location: ../");
}

?>