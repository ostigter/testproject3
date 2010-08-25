xquery version "1.0";

declare option exist:serialize "method=xhtml media-type=text/xml";

import module namespace request="http://exist-db.org/xquery/request"; 

declare function local:greeting($name as xs:string) {
	let $greeting :=
		if ($name = "") then
			"Hello!"
		else
    		concat("Hello, ", $name, "!")
	return
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>HelloWorld from XQuery</title>
                <link rel="stylesheet" type="text/css" href="hello.css" />
            </head>
            <body>
                <p>{$greeting}</p>
            </body>
        </html>
};

let $name := request:get-parameter("name", "")
return
	local:greeting($name)
