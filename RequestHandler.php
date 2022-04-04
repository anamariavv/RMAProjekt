<?php
    //get the json object from java
    $data = json_decode(file_get_contents('php://input'), true);
    
    $logfile = fopen("log.txt", "a");
  
    $server = $data["server"];
    $api = $data["api"];
    $url = "https://";

    //TODO finish filling arrays
    $regionsRV = array("eune"=>"eun1", "brazil"=>"br1", "euw"=>"euw1");
    $platformRV = array("eune"=>"europe", "brazil"=>"americas", "euw"=>"europe");

    //create request url depending on data
    if(strcmp($api, "lv4_challenger") == 0) {
        $url .= ($regionsRV[$server] . ".api.riotgames.com/lol/league/v4/challengerleagues/by-queue/" . $data['queue']);
        fwrite($logfile,"Request url: " .  $url . "\n");
    } else if(strcmp($api, "sv4_name")) {
        $url .= ($regionsRV[$server] . ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" . $data['name']);
    }

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

    echo($response);
?>