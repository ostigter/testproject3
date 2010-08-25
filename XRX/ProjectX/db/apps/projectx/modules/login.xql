xquery version "1.0";

declare namespace p = "http://www.projectx.org";
declare namespace h = "http://www.w3.org/1999/xhtml";
declare namespace request = "http://exist-db.org/xquery/request";
declare namespace response = "http://exist-db.org/xquery/response";
declare namespace session = "http://exist-db.org/xquery/session";

let $username := request:get-parameter("username", "")
let $password := request:get-parameter("password", "")
let $user := doc("/db/apps/projectx/data/users.xml")//p:User[p:Username = $username and p:Password = $password]
return
    if (exists($user)) then (
        session:set-attribute("username", $username),
        response:redirect-to(xs:anyURI("../pages/home.xql"))
    ) else (
        session:set-attribute("errors", <h:p>Invalid username/password combination.</h:p>),
        response:redirect-to(xs:anyURI("../pages/login.xql"))
    )
