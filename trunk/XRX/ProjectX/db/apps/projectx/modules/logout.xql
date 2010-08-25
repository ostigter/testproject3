xquery version "1.0";

declare namespace h = "http://www.w3.org/1999/xhtml";
declare namespace session = "http://exist-db.org/xquery/session";
declare namespace response = "http://exist-db.org/xquery/response";

(
    session:invalidate(),
    session:create(),
    session:set-attribute("messages", <h:p>You have been successfully logged out.</h:p>),
    response:redirect-to(xs:anyURI("../pages/login.xql"))
)
