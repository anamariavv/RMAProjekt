<?php
    require "database.php";

    $data = json_decode(file_get_contents('php://input'), true);
    $response = "ACCESS_DENIED";

    function respond($response) {
        echo json_encode(array("response"=>$response));
        //exit($message);
    }

    if(strcmp($data["source"], "register") == 0) {
        $password = mysqli_real_escape_string($connection, $data["password"]);
        $name = mysqli_real_escape_string($connection, $data["name"]);
        $lastname = mysqli_real_escape_string($connection, $data["lastname"]);
        $email = mysqli_real_escape_string($connection, $data["email"]);
        $username = mysqli_real_escape_string($connection, $data["username"]);       
  
        //check data
        if(!check_empty($data)) {
            respond("EMPTY_VALUES");
        } 
    
        //check for taken username/email
        if(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            respond("INVALID_EMAIL");
        }

        $result = database_select("user", "*", array("email"), array($email), "s");
        if($result) {
            respond("EMAIL_TAKEN");
        }

        $result = database_select("user", "*", array("username"), array($username), "s");
        if($result) {
            respond("USERNAME_TAKEN");
        }

        //validate password
        if(!preg_match($password_pattern, "/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/")) {
            respond("INVALID_PASS");
        } 
       
        $hashed_password = password_hash($password, PASSWORD_BCRYPT);

        //insert new user
        $response = database_insert("user", array("name", "lastname", "email", "password", "username"),
                    "sssss", array( $name, $lastname, $email, $hashed_password, $username));

    
        fclose($logfile);
        respond("200");

    } else if(strcmp($data["source"], "login") == 0) {
        $password = mysqli_real_escape_string($connection, $data["password"]);
        $username = mysqli_real_escape_string($connection, $data["username"]);

        if(!check_empty($data)) {
            respond("EMPTY_VALUES");
        } 

        //check if entry exists by username
        $result = database_select("user", "*", array("username"), array($username), "s");

        if(!password_verify($password, $result["password"])) {
            respond("WRONG_PASSWORD");
        } 
                    
        fclose($logfile);
        respond("200");

    } else {
        respond($response);
    }
?>