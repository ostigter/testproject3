xquery version "1.0";

declare namespace p = "http://www.projectx.org";
declare namespace h = "http://www.w3.org/1999/xhtml";
declare namespace request = "http://exist-db.org/xquery/request";
declare namespace response = "http://exist-db.org/xquery/response";
declare namespace session = "http://exist-db.org/xquery/session";

let $username := request:get-parameter("username", "")
let $password := request:get-parameter("password", "")
let $password2 := request:get-parameter("password2", "")
let $email := request:get-parameter("email", "")
return
    if (string-length($username) eq 0) then (
        session:set-attribute("errors", <h:p>Please fill in a username.</h:p>),
        response:redirect-to(xs:anyURI("../pages/createAccount.xql"))
    ) else if (string-length($password) eq 0) then (
        session:set-attribute("errors", <h:p>Please fill in a password.</h:p>),
        response:redirect-to(xs:anyURI("../pages/createAccount.xql"))
    ) else if ($password != $password2) then (
        session:set-attribute("errors", <h:p>Passwords are not identical.</h:p>),
        response:redirect-to(xs:anyURI("../pages/createAccount.xql"))
    ) else if (string-length($email) eq 0) then (
        session:set-attribute("errors", <h:p>Please fill in an email address.</h:p>),
        response:redirect-to(xs:anyURI("../pages/createAccount.xql"))
    ) else (
        session:set-attribute("username", $username),
        session:set-attribute("messages", <h:p>Your account has been created successfully.</h:p>),
        response:redirect-to(xs:anyURI("../pages/home.xql"))
    )
