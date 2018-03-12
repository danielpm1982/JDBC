<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            body{
                background-color: black;
                text-align: center;
            }
            header{
                color: white;
                font-size: 1.5em;
                font-family: monospace;
                font-weight: bolder;
                line-height: 2;
                width: 50%;
                margin: auto;
                margin-top: 2.5em;
                border: dashed brown 0.2em;
            }
            input#cell0, input#cell1, input#cell2, input#cell3, input#cell4, input#cell5, input#cell6{
                background-color: wheat;
                font-size: 1.5em;
                font-family: sans-serif;
                vertical-align: middle;
                border: solid brown 0.2em;
                display: table-cell;
                width: 43.5%;
                line-height: 1.5;
                margin-top: 0px;
            }
            input#cell1, input#cell3, input#cell5{
                margin-right: 0.5em;
            }
            input#cell2, input#cell4{
                margin-left: 0.5em;
            }
            input#cell0, input#cell2, input#cell3, input#cell4, input#cell5{
                display: inline;
                line-height: 3;
                text-align: center;
            }
            input#cell6{
                display: inline;
                line-height: 3;
                background-color: lightgrey;
                margin-right: 0.3em;
                margin-left: 0.7em;
                border: none;
                width: 43%;
                font-weight: bolder;
            }
            div#table{
                display: table;
                border-collapse: separate;
                border-spacing: 2em;
                width: 50%;
                margin: auto;
                margin-top: 2em;
                margin-bottom: 2em;
                border: solid brown 0.2em;
            }
            div#rowZero, div#rowOne, div#rowTwo, div#rowThree{
                display: table-row;
            }
            input[type='text']{
                font-size: 1.3em;
                width: 15em;
                height: 3em;
                text-align: center;
                margin-top: 2em;
            }
        </style>
    </head>
    <body>
        <header>Fill out the form below for updating an existing Client with new values:</header>
        <form method="post" action="serv.do?command=update">
            <div id="table">
                <div id="rowZero">
                    <input type="number" id="cell0" name="id" placeholder="idOldClient" title="id of the existing Client that will be updated" required />
                </div>
                <div id="rowOne">
                    <input type="text" id="cell1" name="name" placeholder="name" title="new name" required />
                    <input type="number" id="cell2" step="0.01" max="9999999999.99" name="salary" placeholder="salary" title="new salary" required />
                </div>
                <div id="rowTwo">
                    <input type="date" id="cell3" name="birthDate" title="new birthDate" required />
                    <input type="datetime-local" id="cell4" name="registeredIn" title="new registeredIn" required />
                </div>
                <div id="rowThree">
                    <input type="number" id="cell5" name="departmentNo" placeholder="departmentNo" title="new departmentNo" required />
                    <input type="submit" id="cell6" value="Update Client" title='update client' />
                </div>
            </div>
        </form>
        <script>
            for(i=-1;i<5;i++){
                document.getElementById("cell"+(i+1)).onfocus = function(){
                    this.style.backgroundColor="lightyellow";
                    this.style.color="darkgreen";
                };
                document.getElementById("cell"+(i+1)).onblur = function(){
                    this.style.backgroundColor="";
                    this.style.color="";
                };
            };
            var header = document.getElementsByTagName("header");
            header[0].onmouseenter = function(){
                this.style.backgroundColor="lightyellow";
                this.style.color="darkgreen";
            };
            header[0].onmouseout = function(){
                this.style.backgroundColor="";
                this.style.color="";
            };
        </script>
    </body>
</html>
