<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $database = "leaguestatdata";

    $connection = mysqli_connect($servername, $username, $password, $database);

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

    function database_select($table, $columns, $key, $params, $bind_string) {
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
            $row = mysqli_fetch_assoc($result);
        }

        return $row;
    }
?>