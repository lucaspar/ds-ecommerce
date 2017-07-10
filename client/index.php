<?php session_start(); ?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>E-commerce Client</title>
</head>
<body>

    <div class="wrapper">

        <div class="header">
            <h2><span class="fa fa-lg fa-shopping-cart"></span> e-Commerce</h2>
        </div>

        <div class="container">

            <section class="row">
                <div class="col-lg-4 col-md-5 col-sm-6 col-xs-12 offset-lg-2 offset-md-1">
                    <a href="#" onclick="event.preventDefault(); callPlaceOrder()">
                        <div class="card" id="placecard">
                            <h3 class="card-title">Fazer um pedido</h3>
                        </div>
                    </a>
                </div>
                <div class="col-lg-4 col-md-5 col-sm-6 col-xs-12">
                    <a href="#" onclick="event.preventDefault(); callCheckOrder()">
                        <div class="card" id="checkcard" >
                            <h3 class="card-title">Checar um pedido</h3>
                        </div>
                    </a>
                </div>
            </section>

            <section class="row hidden" id="placeForm">
                <div class="col-lg-6 col-md-8 col-sm-12 col-xs-12 offset-lg-3 offset-md-2">
                    <form class="form" action="placeOrder.php" method="POST">
                        <input class="" name="prodID" type="text" placeholder="ID do produto" required>
                        <input class="" name="quantity" type="number" placeholder="Quantidade" min="1" max="20" required>
                        <button class="btn btn-primary" name="submit" type="submit" required>Pedir &nbsp<span class="fa fa-lg fa-shopping-basket"></span></submit>
                    </form>
                </div>
            </section>

            <section class="row hidden" id="checkForm">
                <div class="col-lg-6 col-md-8 col-sm-12 col-xs-12 offset-lg-3 offset-md-2">
                    <form class="form" action="checkOrder.php" method="POST">
                        <input class="" name="orderID" type="text" placeholder="ID do pedido" required>
                        <button class="btn btn-primary" type="submit" required>Checar &nbsp<span class="fa fa-lg fa-check"></span></submit>
                    </form>
                </div>
            </section>

            <!--<input type="button" value="Soap" onclick="soap();" />-->
        </div>

    </div>

    <?php
        if(isset($_SESSION['response'])) {
            echo "<div class='response'>".$_SESSION["response"]." <span class='fa fa-close close'></span></div>";
            unset($_SESSION['response']);
        }
    ?>

    <footer><small>&copy Lucas Parzianello 2017</small></footer>

</body>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha.6/css/bootstrap.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway:300,400">
<link rel="stylesheet" href="css/main.css">

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha.6/js/bootstrap.min.js"></script>
<!--<script type="text/javascript" src="js/jquery.soap.js"></script>-->

<script type="text/javascript">
    function soap() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open('POST', 'http://localhost.com/ws_ecommerce/ws?wsdl', true);

        // build SOAP request
        var sr =
            '<?xml version="1.0" encoding="utf-8"?>' +
            '<soapenv:Envelope ' +
                'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ' +
                'xmlns:api="http://127.0.0.1/Integrics/Enswitch/API" ' +
                'xmlns:xsd="http://www.w3.org/2001/XMLSchema" ' +
                'xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">' +
                '<soapenv:Body>' +
                    '<api:some_api_call soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">' +
                        '<username xsi:type="xsd:string">login_username</username>' +
                        '<password xsi:type="xsd:string">password</password>' +
                    '</api:some_api_call>' +
                '</soapenv:Body>' +
            '</soapenv:Envelope>';

        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4) {
                if (xmlhttp.status == 200) {
                    alert('done. use firebug/console to see network response');
                }
                alert('nah');
            }
        }
        // Send the POST request
        xmlhttp.setRequestHeader('Content-Type', 'text/xml');
        xmlhttp.send(sr);
        // send request
        // ...
    }
</script>

<script type="text/javascript">

$(".close").click(function() {
    $(this).parent().fadeOut(200, function() { $(this).remove();});
});

function callPlaceOrder() {
    $("#placeForm").show(200);
    $("#checkForm").hide(200);
}
function callCheckOrder() {
    $("#placeForm").hide(200);
    $("#checkForm").show(200);
}
</script>
</html>
