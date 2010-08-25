xquery version "1.0";

declare namespace session = "http://exist-db.org/xquery/session";

declare option exist:serialize "method=xhtml media-type=text/html indent=yes omit-xml-declaration=no";

let $errors := session:get-attribute("errors")
let $messages := session:get-attribute("messages")
let $dummy := if (session:exists()) then (session:remove-attribute("errors"), session:remove-attribute("messages")) else ()
return
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Project X</title>
        </head>
        <body>
            <h2>Login</h2>
            <div style="color: red;   font-weight: bold">{$errors}</div>
            <div style="color: green; font-weight: bold">{$messages}</div>
            <form method="POST" action="../modules/login.xql">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" /></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="secret" name="password" /></td>
                    </tr>
                </table>
                <input type="submit" value="Login" />
                <a href="home.xql">Cancel</a>        
            </form>
        </body>
    </html>
