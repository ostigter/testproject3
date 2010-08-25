xquery version "1.0";

declare namespace session = "http://exist-db.org/xquery/session";

declare option exist:serialize "method=xhtml media-type=text/html indent=yes omit-xml-declaration=no";

let $username := session:get-attribute("username")
return
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Project X</title>
        </head>
        <body>
            <h2>Home</h2>
            {
                if (exists($username)) then (
                    <p>Welcome, {$username}.</p>,
                    <p><a href="editAccount.xql">Edit Account</a></p>,
                    <p><a href="../modules/logout.xql">Log out</a></p>
                ) else (
                    <p>Welcome, guest.</p>,
                    <p>Please <a href="login.xql">login</a>, or <a href="createAccount.xql">create</a> a user account.</p>
                )
            }
        </body>
    </html>
