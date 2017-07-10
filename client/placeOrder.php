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

if(!is_numeric($_POST['quantity'])) {
    $_SESSION["error"] = "A quantidade deve ser um número";
    header("Location: index.php");
    die();
}

$wsdl_url = "http://localhost:8080/ws_ecommerce/ws?wsdl";
$client = new SoapClient($wsdl_url, array('trace' => true, 'exceptions' => false));

logme($client->__getTypes());
logme($client->__getFunctions());

$xml_array['arg0'] = htmlspecialchars(strtoupper($_POST['prodID']));
$xml_array['arg1'] = htmlspecialchars($_POST['quantity']);

$response = $client->placeOrder($xml_array);

logme($response->return);

$_SESSION["response"] = "ID do pedido: ".$response->return;

header("Location: index.php");
die();

?>
