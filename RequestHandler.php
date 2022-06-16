<?php
    //get the json object from java
    $data = json_decode(file_get_contents('php://input'), true);
    
    //TODO finish filling arrays
    $platformRV = array("eune"=>"eun1", "brazil"=>"br1", "euw"=>"euw1");
    $regionRV = array("eune"=>"europe", "brazil"=>"americas", "euw"=>"europe");

    $logfile = fopen("log.txt", "a");
  
    function sendRequest($url) {
        //prepare and send request
        $APIfile = fopen("CAcerts/key.txt","r");
        $APIkey = fgets($APIfile);
        fclose($APIfile);
        $header = "X-Riot-Token: " . $APIkey;

        $curlObj = curl_init($url);

        curl_setopt($curlObj, CURLOPT_HTTPHEADER, array($header));
        curl_setopt($curlObj, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curlObj, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
        curl_setopt($curlObj, CURLOPT_SSL_VERIFYPEER, true);
        curl_setopt($curlObj, CURLOPT_SSL_VERIFYHOST, 2);
        curl_setopt($curlObj, CURLOPT_CAINFO, getcwd() . "/CAcerts/developer-riotgames-com-chain.pem");

        $response = curl_exec($curlObj);
        $info = curl_getinfo($curlObj);
        $error = curl_error($curlObj);
        $code = curl_errno($curlObj);
        curl_close($curlObj);

        return $response;

    }

    function sv4_summoner_by_name($data) {
        $routing_value = $GLOBALS['platformRV'];
        $url = "https://" . $routing_value[$data["server"]];
        $url .= ".api.riotgames.com/lol/summoner/v4/summoners/by-name/";

        $formatted_name = explode(" ", $data['name']);
        $formatted_name = implode("%20", $formatted_name);

        $url .= $formatted_name;

        $response = sendRequest($url);

        return $response;
    }

    function lv4_challenger($data) {
        $routing_value = $GLOBALS['platformRV'];
        $url = "https://" . $routing_value[$data["server"]];
        $url .= ".api.riotgames.com/lol/league/v4/challengerleagues/by-queue/";
        $url .= $data['queue'];

        $response = sendRequest($url);

        return $response;
    }

    function mv5_matches_by_puuid($data) {
        $routing_value = $GLOBALS['regionRV'];
        $url = "https://" . $routing_value[$data["server"]];
        $url .= ".api.riotgames.com/lol/match/v5/matches/by-puuid/".$data["puuid"]."/ids?start=0&count=10";

        $response = sendRequest($url);

        return $response;
    }

    function mv5_matches_by_matchid($data) {
        $routing_value = $GLOBALS["regionRV"];
        $url = "https://" . $routing_value[$data["server"]];
        $url .= ".api.riotgames.com/lol/match/v5/matches/".$data["matchid"];

        $response = sendRequest($url);

        return $response;
    }

   function format_json($json_array, $puuid) {
        unset($json_array['metadata']['participants']);
        unset($json_array['info']['teams']);

        date_default_timezone_set('Europe/Berlin');
        $timestamp = $json_array['info']['gameCreation'];
        unset($json_array['info']['gameCreation']);
        $json_array['info']['gameCreation'] = date('d-m-Y', $timestamp/1000);

        //fwrite($GLOBALS['logfile'], "\ndate: ".$json_array['info']['gameCreation']);

        //for each participant, check which one is target
        for($i=0; $i < 10; $i++) { 
            if(strcmp($puuid, $json_array['info']['participants'][$i]['puuid']) != 0) {
                unset($json_array['info']['participants'][$i]);
            } else {
                //if its a match, unset data i dont need
                unset($json_array['info']['participants'][$i]['challenges']);
                $target_participant = $json_array['info']['participants'][$i];
                unset($json_array['info']['participants'][$i]);
                $json_array['info']['participants']["0"] = $target_participant;
                
            }
        }
    
        //$result = print_r($json_array, true);
        //fwrite($GLOBALS['logfile'], "\n---------Cropped json data------------------\n".$result);

        return $json_array;
   }

    $response;

    if(strcmp( $data["api"], "lv4_challenger") == 0) {
        $response = lv4_challenger($data);
    } else if(strcmp($data["api"], "sv4_name") == 0) {
        $response = sv4_summoner_by_name($data);
    } else if(strcmp($data["api"], "summoner_match_history") == 0) {
        $summoner_info = sv4_summoner_by_name($data);
        $summoner_info_decoded = json_decode($summoner_info, TRUE);

        $data["puuid"] = $summoner_info_decoded["puuid"];

        $match_ids = mv5_matches_by_puuid($data);
        $match_ids_decoded = json_decode($match_ids, TRUE);
    
        $matches_array = array();

        foreach($match_ids_decoded as $id) {
            $data["matchid"] = $id;
            $match_info = mv5_matches_by_matchid($data);
            $match_info_formatted = format_json(json_decode($match_info, TRUE), $data["puuid"]);
            array_push($summoner_info_decoded, $match_info_formatted);
        }     
     
        $response = json_encode($summoner_info_decoded); 
       // fwrite($logfile, "\n". $response);
    }

    echo($response);
?>