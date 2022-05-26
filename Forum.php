<?php
    require "database.php";

    $data = json_decode(file_get_contents('php://input'), true);
    $response = "ACCESS_DENIED";

    if(strcmp($data["source"], "new post") == 0) {

        if(check_empty($data)) {
            respond("EMPTY_VALUES");
        }

        $title = mysqli_real_escape_string($connection, $data["title"]);
        $text = mysqli_real_escape_string($connection, $data["text"]);
        $author = mysqli_real_escape_string($connection, $data["author"]);

        unset($data["source"]);
        
        $result = database_insert("forum_topic", array_keys($data), "sss", array_values($data));
        respond("200");
    } else {
        respond($response);
    }
?>