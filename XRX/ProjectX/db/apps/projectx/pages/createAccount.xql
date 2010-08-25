xquery version "1.0";

declare namespace response = "http://exist-db.org/xquery/response";

declare option exist:serialize "method=xhtml media-type=text/html indent=yes omit-xml-declaration=no";

let $errors := session:get-attribute("errors")
let $messages := session:get-attribute("messages")
let $dummy := (session:remove-attribute("errors"), session:remove-attribute("messages"))
return
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Project X</title>
        </head>
        <body>
            <h2>Create Account</h2>
            <div style="color: red;   font-weight: bold">{$errors}</div>
            <div style="color: green; font-weight: bold">{$messages}</div>
            <form method="POST" action="../modules/createAccount.xql">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" /></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="secret" name="password" /></td>
                    </tr>
                    <tr>
                        <td>Password (again):</td>
                        <td><input type="secret" name="password2" /></td>
                    </tr>
                    <tr>
                        <td>Email address:</td>
                        <td><input type="text" name="email" /></td>
                    </tr>
                </table>
                <input type="submit" value="Create Account" />
                <a href="home.xql">Cancel</a>
            </form>
        </body>
    </html>
