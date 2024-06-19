<?php
//This script creates a new user and returns his access token if success, dont forget to save this access token

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "https://api.cognifit.com/registration");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
curl_setopt($ch, CURLOPT_HEADER, FALSE);

curl_setopt($ch, CURLOPT_POST, TRUE);

curl_setopt($ch, CURLOPT_POSTFIELDS, "{
  \"client_id\": \"xxx\", // set your clientid
  \"client_secret\": \"xxx\", // set your clientid
  \"user_name\": \"xxx\", // set your username
  \"user_lastname\": \"xxx\", // set your lastname
  \"user_email\": \"xxx\", // set your email
  \"user_password\": \"123456\",  // set your password
  \"user_birthday\": \"1999-03-07\", // set your birthday
  \"user_sex\": 1, // set your sex: 1 male, 2 females, 3 other
  \"user_locale\": \"en\" // set your language
}");

curl_setopt($ch, CURLOPT_HTTPHEADER, array(
  "Content-Type: application/json;",
  "cache-control: no-cache;"
));

$response = curl_exec($ch);
curl_close($ch);

var_dump($response);