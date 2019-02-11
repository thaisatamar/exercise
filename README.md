# exercise
Large file upload using Jetty Web Server over Spring Boot

Small client / server application leveraging Jetty Web Server over Spring Boot to upload large files over the network.


To run the server on the default interface and port 8080:

source file-server &

To run the client:

source file-client [file-name]


The server may be safely stopped using the kill -15 command, or CTRL-C if running on the foreground.

To configure the client over a different host and port, edit the client.yml file on the etc directory with the desired details. The rationale here is that the customer would configure this once, then forget about this setting.

To disable compression (for already compressed files, for example), write “no” or “false” as the second parameter:

source file-client /Users/me/somefile no

