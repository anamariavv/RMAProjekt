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

   function replace_summoner_spell_id($summoner_spell_id) {
    $summoner_spell_name = "";

        switch ($summoner_spell_id) {
            case 21:
                    $summoner_spell_name .= "SummonerBarrier";
                break;
            case 1:
                    $summoner_spell_name .= "SummonerBoost";
                break;
            case 14:
                    $summoner_spell_name .= "SummonerDot";
                break;
            case 3:
                    $summoner_spell_name .= "SummonerExhaust";
                break;
            case 4:
                    $summoner_spell_name .= "SummonerFlash";
                break;
            case 6:
                    $summoner_spell_name .= "SummonerHaste";
                break;
            case 7:
                    $summoner_spell_name .= "SummonerHeal";
                break;
            case 15:
                    $summoner_spell_name .= "SummonerMana";
                break;
            case 30:
                    $summoner_spell_name .= "SummonerPoroRecall";
                break;
            case 31:
                    $summoner_spell_name .= "SummonerPoroThrow";
                break;
            case 11:
                    $summoner_spell_name .= "SummonerSmite";
                break;
            case 39:
                    $summoner_spell_name .= "SummonerSnowURFSnowball_Mark";
                break;
            case 32:
                    $summoner_spell_name .= "SummonerSnowball";
                break;
            case 12:
                    $summoner_spell_name .= "SummonerTeleport";
                break;
            case 54:
                    $summoner_spell_name .= "Summoner_UltBookPlaceholder";
                break;
            case 55:
                    $summoner_spell_name .= "Summoner_UltBookSmitePlaceholder";
                break;  
        }

    return $summoner_spell_name;
   }

   function replace_perk_id($perk_id) {
        $perk_path = "";

        switch ($perk_id) {
            case 8100:
                    $perk_path = "perk-images/Styles/7200_Domination.png";
                break;
            case 8000:
                    $perk_path = "perk-images/Styles/7201_Precision.png";
                break;
            case 8200:
                    $perk_path = "perk-images/Styles/7202_Sorcery.png";
                break;
            case 8300:
                    $perk_path = "perk-images/Styles/7203_Whimsy.png";
                break;
            case 8400:
                    $perk_path = "perk-images/Styles/7204_Resolve.png";
                break;
            case 8112:
                $perk_path = "perk-images/Styles/Domination/Electrocute/Electrocute.png";
            break;
            case 8124:
                $perk_path = "perk-images/Styles/Domination/Predator/Predator.png";
            break;
            case 8128:
                $perk_path = "perk-images/Styles/Domination/DarkHarvest/DarkHarvest.png";
            break;
            case 9923:
                $perk_path = "perk-images/Styles/Domination/HailOfBlades/HailOfBlades.png";
            break;
            case 8008:
                $perk_path = "perk-images/Styles/Precision/LethalTempo/LethalTempoTemp.png";
            break;
            case 8005:
                $perk_path = "perk-images/Styles/Precision/PressTheAttack/PressTheAttack.png";
            break;
            case 8021:
                $perk_path = "perk-images/Styles/Precision/FleetFootwork/FleetFootwork.png";
            break;
            case 8010:
                $perk_path = "perk-images/Styles/Precision/Conqueror/Conqueror.png";
            break;
            case 8214:
                $perk_path = "perk-images/Styles/Sorcery/SummonAery/SummonAery.png";
            break;
            case 8229:
                $perk_path = "perk-images/Styles/Sorcery/ArcaneComet/ArcaneComet.png";
            break;
            case 8230:
                $perk_path = "perk-images/Styles/Sorcery/PhaseRush/PhaseRush.png";
            break;
            case 8437:
                $perk_path = "perk-images/Styles/Resolve/GraspOfTheUndying/GraspOfTheUndying.png";
            break;
            case 8439:
                $perk_path = "perk-images/Styles/Resolve/VeteranAftershock/VeteranAftershock.png";
            break;
            case 8465:
                $perk_path = "perk-images/Styles/Resolve/Guardian/Guardian.png";
            break;
            case 8351:
                $perk_path = "perk-images/Styles/Inspiration/GlacialAugment/GlacialAugment.png";
            break;
            case 8360:
                $perk_path = "pperk-images/Styles/Inspiration/UnsealedSpellbook/UnsealedSpellbook.png";
            break;
            case 8369:
                $perk_path = "perk-images/Styles/Inspiration/FirstStrike/FirstStrike.png";
            break;           
        }

        return $perk_path;
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
                //if its a match
                if(strcmp($json_array['info']['participants'][$i]['championName'], "FiddleSticks") == 0) {
                    $json_array['info']['participants'][$i]['championName'] = "Fiddlesticks";
                }

                //replace summoner spell ids with their name
                for ($spellNum =1; $spellNum <=2 ; $spellNum++) { 
                    $summoner_spell_name =  replace_summoner_spell_id($json_array['info']['participants'][$i]['summoner'.$spellNum.'Id']);
                    $json_array['info']['participants'][$i]['summoner'.$spellNum.'Id'] = $summoner_spell_name;
                }

                //replace perk ids with url path
                $perk_id = $json_array['info']['participants'][$i]['perks']['styles'][0]['selections'][0]['perk'];
                $perk_id_secondary = $json_array['info']['participants'][$i]['perks']['styles'][1]['style'];

                unset($json_array['info']['participants'][$i]['perks']);
                $json_array['info']['participants'][$i]['perks']['primary'] = replace_perk_id($perk_id);
                $json_array['info']['participants'][$i]['perks']['secondary'] = replace_perk_id($perk_id_secondary);
               
                //unset useless data                      
                unset($json_array['info']['participants'][$i]['challenges']);
                $target_participant = $json_array['info']['participants'][$i];
                unset($json_array['info']['participants'][$i]);
                $json_array['info']['participants']['0'] = $target_participant;
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

        if(!array_key_exists("status", $summoner_info_decoded)) {
            $data["puuid"] = $summoner_info_decoded["puuid"];
            fwrite($logfile, "status is found\n");  

            $match_ids = mv5_matches_by_puuid($data);
            $match_ids_decoded = json_decode($match_ids, TRUE);
        
            $matches_array = array();
    
            foreach($match_ids_decoded as $id) {
                $data["matchid"] = $id;
                $match_info = mv5_matches_by_matchid($data);
                $match_info_formatted = format_json(json_decode($match_info, TRUE), $data["puuid"]);
                array_push($summoner_info_decoded, $match_info_formatted);
            }  
               
        } else {
            fwrite($logfile, "status is not found\n");  
        }      
     
        $response = json_encode($summoner_info_decoded); 
       // fwrite($logfile, "\n". $response);
    }

    echo($response);
?>