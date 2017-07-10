<?php

session_start();

ini_set('display_errors', true);
error_reporting(E_ALL);

function logme($data) {
    echo "<p>";
    if(is_array($data)){
        for ($k=0; $k<count($data); $k++) {
            # code...
            print_r($data[$k]);
            echo "<br>";
        }
    }
    else {
        print_r($data);
    }

    echo "<hr></p>";
}

$wsdl_url = "http://localhost:8080/ws_ecommerce/ws?wsdl";
$client = new SoapClient($wsdl_url, array('trace' => true, 'exceptions' => false));

logme($client->__getTypes());
logme($client->__getFunctions());

$xml_array['arg0'] = htmlspecialchars($_POST['orderID']);

$response = $client->checkOrder($xml_array);
//$response = $client->init();

logme($response->return);

$_SESSION["response"] = $response->return;

header("Location: index.php");
die();

?>
