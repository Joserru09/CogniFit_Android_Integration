<?php
// Obtains post parametres
$client_id = $_POST['client_id'];
$client_secret = $_POST['client_secret'];
$user_token = $_POST['user_token'];
$session_id = $_POST['session_id'];

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, "https://api.cognifit.com/user-info/get-assessment-values");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
curl_setopt($ch, CURLOPT_HEADER, FALSE);
curl_setopt($ch, CURLOPT_POST, TRUE);

curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode(array(
    "client_id" => $client_id,
    "client_secret" => $client_secret,
    "user_token" => $user_token,
    "session_id" => $session_id
)));

curl_setopt($ch, CURLOPT_HTTPHEADER, array(
  "Content-Type: application/json;",
  "cache-control: no-cache;"
));

$response = curl_exec($ch);
curl_close($ch);

// returns response in JSON
header('Content-Type: application/json');
echo $response;
?>
