<%-- 
    Document   : index
    Created on : 2015-feb-18, 15:17:55
    Author     : Jakob
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <base href="${pageContext.request.contextPath}/" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Start page</h1>
        <a href="test">Test</a>
        <br>
        <a href="test2">Test2</a>
        <br><br>
        <a href="users">
            I denna länken kommer JSON-representation av ett objekt visas.
            <br>Det kommer välja default-värden till en början. Man kan dock
            <br>specifiera data som ska behandlas av controllern med att sätta
            <br>?userId=something&userName=something <br>efter URLen.
        </a>
        
        <p>Man kan dessutom genom att gå till /users/something accessa specifika
            <br>sidor, och sedan få det man skrev in skickat tillbaka
            <br>genom JSON.
        </p>
        <p>
        <h1><a href="/bokforing/public/static/index.html">LOGIN</a></h1>
        </p>
    </body>
</html>
