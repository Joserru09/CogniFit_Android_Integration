

<?php

// This script obtain the access token


$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, "https://api.cognifit.com/issue-access-token");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
curl_setopt($ch, CURLOPT_HEADER, FALSE);

curl_setopt($ch, CURLOPT_POST, TRUE);

curl_setopt($ch, CURLOPT_POSTFIELDS, "{
  \"client_id\": \"xxx\", // set your clientid
  \"client_secret\": \"xxx\", // set your clientsecret
  \"user_token\": \"xxx\" // set your user_token
}");

curl_setopt($ch, CURLOPT_HTTPHEADER, array(
  "Content-Type: application/json;",
  "cache-control: no-cache;"
));

$response = curl_exec($ch);
curl_close($ch);

$data = json_decode($response, true); // JSON reposnse decoder

if (isset($data['access_token'])) {
    echo $data['access_token']; // Return just the access token as response
} else {
    echo "Error obtaining access token";
}
?>