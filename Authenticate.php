<?php
    require "database.php";

    $data = json_decode(file_get_contents('php://input'), true);
    $response = "ACCESS_DENIED";

    if(strcmp($data["source"], "register") == 0) {
        //check data
        if(!check_empty($data)) {
            respond("EMPTY_VALUES");
        } 

        $password = mysqli_real_escape_string($connection, $data["password"]);
        $name = mysqli_real_escape_string($connection, $data["name"]);
        $lastname = mysqli_real_escape_string($connection, $data["lastname"]);
        $email = mysqli_real_escape_string($connection, $data["email"]);
        $username = mysqli_real_escape_string($connection, $data["username"]);       
  
        //validate password
        if(!preg_match('/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/', $password)) {
            respond("INVALID_PASS");
        } 
       
        if(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            respond("INVALID_EMAIL");
        }

        //check for taken username/email
        $result = database_select("user", "*", array("email"), array($email), "s", false);
        if($result) {
            respond("EMAIL_TAKEN");
        } else {
            $result = database_select("user", "*", array("username"), array($username), "s", false);
            if($result) {
                respond("USERNAME_TAKEN");
            } else {
                $hashed_password = password_hash($password, PASSWORD_BCRYPT);
                //insert new user
                $response = database_insert("user", array("name", "lastname", "email", "password", "username"),
                        "sssss", array( $name, $lastname, $email, $hashed_password, $username));
                respond($response);

            }
        }
   
        fclose($logfile);
    } else if(strcmp($data["source"], "login") == 0) {

        if(!check_empty($data)) {
            respond("EMPTY_VALUES");
        } 

        $password = mysqli_real_escape_string($connection, $data["password"]);
        $username = mysqli_real_escape_string($connection, $data["username"]);

        //check if entry exists by username
        $result = database_select("user", "*", array("username"), array($username), "s", false);

        if(!$result) {
            respond("DOESN'T_EXIST");
        }

        if(!password_verify($password, $result["password"])) {
            respond("WRONG_PASSWORD");
            fwrite($GLOBALS["logfile"], "\npasswords: " .$password."--".$result["password"]);
        } 
                    
        fclose($logfile);
        respond("200");

    } else {
        respond($response);
    }
?>