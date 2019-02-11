# Large file upload using Jetty Web Server over Spring Boot

Small client / server application leveraging Jetty Web Server over Spring Boot to upload large files over the network.


## Running
To run the server on port 8080:

<p><code>source file-server &</code></p>

To run the client:

<p><code>source file-client [file-name]</code></p>

To disable compression (for already compressed files, for example), write “no” or “false” as the second parameter:

<p><code>source file-client /Users/me/somefile no</code></p>



## Configuring

To configure the client over a different host and port, edit the client.yml file on the etc directory with the desired details. 

## Additional instructions

Server options

To run the server over a different port:

<p><code>source file-server [newport] &</code></p>

To run the server over a different port / network interface (this should be rarely required):

<p><code>source file-server [newport] [hostname] &</code></p>




