<?php
    require "database.php";

    $data = json_decode(file_get_contents('php://input'), true);
    $response = "ACCESS_DENIED";

    
    if(strcmp($data["source"], "display") == 0) {
        $username = mysqli_real_escape_string($connection, $data["username"]);

        if(!check_empty($data)) {
            respond("EMPTY_VALUES");
        } 

        $response = database_select("user", "*", array("username"), array($username), "s", false);

        if(!$response) {
            respond("DOESN'T EXIST");
        }
    
       respond($response);
    } else if(strcmp($data["source"], "edit") == 0) {
        $old_username = mysqli_real_escape_string($connection, $data["old_username"]);  

        if(!check_empty($data)) {
            respond("EMPTY_VALUES");
        } 

        if(array_key_exists("email", $data) && !filter_var($data["email"], FILTER_VALIDATE_EMAIL)) {
            respond("INVALID_EMAIL");
        }


        fwrite($GLOBALS["logfile"], "\nInsert response is: " . implode("--", array_keys($data)));
        fwrite($GLOBALS["logfile"], "\nInsert response is2: " . implode("--", array_values($data)));

        //if email is set-> check it, if username is set->check it
        $email_ok = true;
        $username_ok = true;

        if(array_key_exists("email", $data)) {
            $result_email = database_select("user", "*", array("email"), array($data["email"]), "s", false);        
            
            if($result_email) {
                respond("EMAIL_TAKEN");
                $email_ok = false;
            }
        }  
        
        if(array_key_exists("username", $data)) {
            $result_username = database_select("user", "*", array("username"), array($data["username"]), "s", false);        
            
            if($result_username) {
                respond("USERNAME_TAKEN");
                $username_ok = false;
            }
        }

        
        if($email_ok && $username_ok) {
            unset($data["source"]);
            unset($data["old_username"]);

            $bind_string = "";
            for ($i=0; $i < count($data)+1; $i++) { 
                $bind_string .= "s";
            }

            $data2 = $data;
            $data2["old_username"] = $old_username;

            fwrite($GLOBALS["logfile"], "\nAfter unset2: " . implode("--", array_values($data2)));

            $response = database_update("user", array_keys($data), array("username"), 
            $bind_string, array_values($data2));

            respond($response);
        } else {
            respond("");
        }
    
    }  else {
        respond($response);
    }
?>