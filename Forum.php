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
    } else if(strcmp($data["source"], "display") == 0) {
        
        $result = database_select("forum_topic", "*", null, null, null, true);
        
        respond_array($result);

    } else if(strcmp($data["source"], "new comment") == 0) {

        if(check_empty($data)) {
            respond("EMPTY_VALUES");
        }

        $text = mysqli_real_escape_string($connection, $data["text"]);
        $forum_topic_id = mysqli_real_escape_string($connection, $data["forum_topic_id"]);
        $author = mysqli_real_escape_string($connection, $data["author"]);

        unset($data["source"]);
        
        $result = database_insert("comment", array_keys($data), "sss", array_values($data));
        respond("200");
    } else if(strcmp($data["source"], "display comments") == 0) {

        if(check_empty($data)) {
            respond("EMPTY_VALUES");
        }

        unset($data["source"]);

        $result = database_select("comment", "*", array("forum_topic_id"), array_values($data), "i", true);

        respond_array(($result));
    } else {
        respond($response);
    }
?>