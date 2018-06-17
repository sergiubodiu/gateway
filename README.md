# Spring Cloud Gateway Sample

Sample that shows a few different ways to route and showcases some filters.

## Samples

HTTP Request & Response Service, written in Python + Flask.
https://httpbin.org/

    scoop install python
    curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
    python get-pip.py
    python -m pip install --upgrade pip setuptools
    pip install --upgrade httpie
 
 
 http http://localhost:8080/get
 
 http :8080/get
 
 http :8080/headers Host:www.myhost.org 
 
 http :8080/foo/get Host:www.rewrite.org
 
 http :8080/foo/get Host:www.setpath.org
 

[Spring Cloud Gateway docs](https://cloud.spring.io/spring-cloud-gateway/single/spring-cloud-gateway.html)
 
 http :8080/delay/3 Host:www.hystrixfallback.org
 
 ## Websocket Sample
 
 [install wscat](https://www.npmjs.com/package/wscat)
 
 In one terminal, run websocket server:
 ```
 wscat --listen 9000
 ``` 
 
 In another, run a client, connecting through gateway:
 ```
 wscat --connect ws://localhost:8080/echo
 ```
 
 type away in either server and client, messages will be passed appropriately.
 
 ## Running Redis Rate Limiter Test
 
 Make sure redis is running on localhost:6379 (using brew or apt or docker).
 
 Then run `DemogatewayApplicationTests`. It should pass which means one of the calls received a 429 TO_MANY_REQUESTS HTTP status.

[WebSocket cat](https://github.com/websockets/wscat)

    npm install -g wscat
    bin\ws_server & 
    bin\ws_client

TBD Rate Limiting