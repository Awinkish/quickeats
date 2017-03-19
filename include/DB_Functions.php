
Editing:  
/home/dawagito/public_html/quickeats/include/DB_Functions.php
 Encoding:    Re-open Use Code Editor     Close  Save Changes

<?php


class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($fname, $sname, $referral ,$mobile, $email, $password, $userid, $address, $latitude, $longitude) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = md5($password); // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, firstname, lastname, refferal_code, phoneno, email, password, salt, reffered_by, formatted_address, address_lat, address_lng, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssssssssssss", $uuid, $fname, $sname, $referral ,$mobile, $email, $encrypted_password, $salt, $userid, $address, $latitude, $longitude);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Storing new agent
     * returns agent details
     */

    public function storeAgent($fname, $sname, $mobile, $email, $password, $address, $latitude, $longitude) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = md5($password); // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO delivery_agents(unique_id, firstname, lastname, phoneno, email, password, salt, formatted_address, current_lat, current_long, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssssssssss", $uuid, $fname, $sname, $mobile, $email, $encrypted_password, $salt, $address, $latitude, $longitude);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM delivery_agents WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

    
        if (is_numeric($email) ) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE phoneno = ?");
        }else{
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
        }
        

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $encrypted_password = $user['password'];
            
            if ($encrypted_password == md5($password)) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Get agent by email and password
     */
    public function getAgentByEmailAndPassword($email, $password) {


        $stmt = $this->conn->prepare("SELECT * FROM delivery_agents WHERE phoneno = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $encrypted_password = $user['password'];
            
            if ($encrypted_password == md5($password)) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email,$table) {
        $stmt = $this->conn->prepare("SELECT email from ". $table ." users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    public function isNumberExisted($mobile,$table) {
        $stmt = $this->conn->prepare("SELECT phoneno from " .$table ." WHERE phoneno = ?");

        $stmt->bind_param("s", $mobile);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

    public function check_who_reffered($referral) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE refferal_code = ?");

        $stmt->bind_param("s", $referral);

        $stmt->execute();
        

        $referral = $stmt->get_result()->fetch_assoc();

        if ($referral) {
            // referral found
            $stmt->close();
            return $referral;
        } else {
            // referral not found
            $stmt->close();
            return false;
        }


    }

   public function getAccountDetails($email) {
       
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return NULL;
        }
    }

   public function updateEntityExclusive($fname, $sname, $mobile, $email, $email_old) {


        $sql = "UPDATE users SET firstname = ".$fname.", lastname =".$sname." , phoneno=".$mobile." , email=".$email." WHERE email = ".$email_old."";

        if ($conn->query($sql) === TRUE) {
            return $user;
        } else {
            return false;
        }

    }

   public function storeAddress($uid, $address, $nickname, $description) {

        $stmt = $this->conn->prepare("INSERT INTO addresses(user_id, formatted_address, nickname) VALUES(?, ?, ?)");
        $stmt->bind_param("sss", $uid, $address, $nickname);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE uid = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

}

?>

