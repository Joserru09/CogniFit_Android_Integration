# Backend Integration for CogniFit

This folder contains optional PHP scripts for integrating with the CogniFit API. Using these backend scripts is recommended for better security practices. 

## Overview

- `register.php`: Script to register a user and obtain a `user_token` (mandatory).
- `accesstoken.php`: Script to obtain an `access_token` using the `user_token`.
- `assessment_values.php`: Script to obtain assessment values.
- `getassessmentreport.php`: Script to obtain the assessment report.

## Why Use Backend Scripts?

Using backend scripts to handle API calls ensures that sensitive information, such as client secrets, are not exposed in the frontend code. This helps in maintaining the security and integrity of your application.

## Getting Started

### Prerequisites

- A PHP server to run the scripts.
- Basic knowledge of PHP and server configuration.

### Setup

1. **Clone the Repository:**
   ```sh
   git clone https://github.com/yourusername/yourrepository.git
   cd yourrepository/backend
2. Update the Scripts:
Open register.php, accesstoken.php, assessment_values.php, and getassessmentreport.php and replace placeholders with your actual CogniFit client_id and client_secret

### Scripts
register.php
This script registers a new user and returns a user_token. This step is mandatory to obtain the user_token required for further API calls.
To execute register.php, navigate to its directory in your PHP server and access it via a web browser or a tool like curl (curl http://yourserver/register.php)
If you dont have server, runs it locally
Make sure to save the user_token returned by this script for future use. It will be return once time the access token


accesstoken.php
This script retrieves an access_token using the user_token.
To execute accesstoken.php, navigate to its directory in your PHP server and access it via a web browser or a tool like curl (curl http://yourserver/access_token.php)
If you dont have server, runs it locally


assessment_values.php
This script retrieves the assessment values for a user.


getassessmentreport.php
This script retrieves the assessment report for a user.