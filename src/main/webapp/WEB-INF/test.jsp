<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
        <title>Sample Application JSP Page</title>
    </head>

    <body bgcolor=white>

        <table border="0" cellpadding="10">
            <tr>
                <td align=center>
                    <img src="images/springsource.png">
                </td>
                <td>
                    <h1>Sample Application JSP Page</h1>
                </td>
            </tr>
        </table>

        <br />
        <p>This is the output of a JSP page that is part of the HelloWorld application.</p>

        <h1>${id}</h1>

    </body>
</html> 