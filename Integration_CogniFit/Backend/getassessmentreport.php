<?php
// Obtain post parametres
$client_id = $_POST['client_id'];
$client_secret = $_POST['client_secret'];
$user_token = $_POST['user_token'];
$session_id = $_POST['session_id'];
$locale = $_POST['locale'];

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, "https://api.cognifit.com/user-info/get-assessment-report");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
curl_setopt($ch, CURLOPT_HEADER, FALSE);
curl_setopt($ch, CURLOPT_POST, TRUE);

curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode(array(
    "client_id" => $client_id,
    "client_secret" => $client_secret,
    "user_token" => $user_token,
    "session_id" => $session_id,
    "locale" => $locale,
    "report_types" => ["ASSESSMENT_REPORT", "COGNITIVE_PROFILE", "CARE_PLAN"]
)));

curl_setopt($ch, CURLOPT_HTTPHEADER, array(
  "Content-Type: application/json;",
  "cache-control: no-cache;"
));

$response = curl_exec($ch);
$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($http_code == 200) {
    // Configure response as a zip archive
    header('Content-Type: application/zip');
    header('Content-Disposition: attachment; filename="assessment_report.zip"');
    echo $response;
} else {
    // Return response in JSON
    header('Content-Type: application/json');
    echo json_encode(array("error" => "Unable to generate report", "details" => $response));
}
?>
