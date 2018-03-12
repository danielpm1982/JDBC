<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            body{
                background-color: black;
                color: white;
                margin: 3em;
                text-align: center;
                font-size: 1.5em;
            }
            h2.command{
                color: lightBlue;
            }
            div.table{
                width: 90%;
                margin: auto;
                margin-top: 3em;
                margin-bottom: 5em;
                display: table;
                font-size: 1em;
                border: dashed bisque medium;
                padding: 1em;
            }
            div.row{
                display: table-row;
            }
            div.cell1, div.cell2{
                display: table-cell;
            }
            div.cell1{
                text-align: right;
                color: bisque;
                padding-right: 0.5em;
            }
            div.cell2{
                text-align: left;
                color: aquamarine;
                padding-left: 0.5em;
            }
        </style>
    </head>
    <body>
        <h1>Response to the JDBC command:</h1>
        <h2 class="command">${command}</h2>
        <h2 class="command">${resultMessage}</h2>
        <h1>Current DataBase state:</h1>
        <h2 class="command">Select * from client</h2>
        <h2 class="command">found ${resultSize} result(s):</h2>
        <div class="table">    
            <c:forEach var="item" items="${requestScope.clientList}" varStatus="count">
                <div class="row">
                    <div class="cell1">${count.count}. </div>
                    <div class="cell2">${item}</div>
                </div>
            </c:forEach>
        </div>
    </body>
</html>
