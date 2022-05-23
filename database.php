<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $database = "leaguestatdata";

    $connection = mysqli_connect($servername, $username, $password, $database);

    mysqli_set_charset($connection, "utf8mb4");

    $logfile = fopen("log.txt", "a");

    if(!$connection) {
        die("could not connect: " . mysqli_connect_error());
    } 

    function check_empty($data_array) {

        foreach ($data_array as $value) {
            if(empty($value)) {
                return false;
            }
        }
        return true;
    }
    
    function respond($response) {
        echo json_encode(array("response"=>$response));
    }

    function database_insert($table, $columns, $bind_string, $params) {
        $sql = "INSERT INTO " . $table . "(" . implode("," , $columns) . ") VALUES (";

        for($i = 0; $i < count($params); $i++) {
            $sql .= "?";
            if($i != count($params)-1) {
                $sql .= ",";
            }
        }

        $sql .= ")";

        if(!$stmt = mysqli_prepare($GLOBALS['connection'], $sql)) {
            $response = "PREPARE_FAILED";
        } 
        if(!mysqli_stmt_bind_param($stmt, $bind_string, ...$params)){
            $response = "BIND_FAILED";
        } 
        if(!mysqli_stmt_execute($stmt)) {
            $response = "EXECUTE_FAILED";
        } else {
            $response = "200";
        }

        return $response;
    }

    function database_select($table, $columns, $key, $params, $bind_string, $all_rows) {
        $sql = "SELECT ";
        $row = "";

        if(strcmp("*", $columns) == 0) {
            $sql .= "*";
        } else {
            $sql .= implode(",", $columns);
        }

        $sql .= " FROM " . $table . " WHERE ";

        for($i = 0; $i < count($key); $i++) {
            $sql .= $key[$i] . "=?";
        }
       // fwrite($GLOBALS["logfile"], "\nsql is ".$sql);
        if(!$stmt = mysqli_prepare($GLOBALS['connection'], $sql)) {
            $response = "PREPARE_FAILED";
        } 
        if(!mysqli_stmt_bind_param($stmt, $bind_string, ...$params)){
            $response = "BIND_FAILED";
        } 
        if(!mysqli_stmt_execute($stmt)) {
            $response = "EXECUTE_FAILED";
        } else {
            $result = mysqli_stmt_get_result($stmt);

            if($all_rows) {
                $response = mysqli_fetch_all($result, MYSQLI_ASSOC); 
            } else {
                $response = mysqli_fetch_assoc($result);
            }          
        }

        return $response;
    }

    //table to update, columns to set, key = where condition, params = instead of ?
    function database_update($table, $columns, $key, $bind_string, $params) {
        $sql = "UPDATE " . $table . " SET ";

        for($i = 0; $i < count($columns); $i++) {
           $sql .= $columns[$i];
           $sql .= "=?";
    
           if($i != count($columns)-1) {
            $sql .= ",";
            }
        }
        
        $sql .= " WHERE ";
        for($i = 0; $i < count($key); $i++) {
            $sql .= $key[$i] . "=?";
            if($i != count($key)-1) {
                $sql .= " AND ";
            }
        }

        fwrite($GLOBALS["logfile"], "\nInsert: " . $sql);

        if(!$stmt = mysqli_prepare($GLOBALS['connection'], $sql)) {
            $response = "PREPARE_FAILED";
        } 
        if(!mysqli_stmt_bind_param($stmt, $bind_string, ...$params)){
            $response = "BIND_FAILED";
        } 
        if(!mysqli_stmt_execute($stmt)) {
            $response = "EXECUTE_FAILED";
        } else {
            $response = "200";
        }

        return $response;
    }

?>