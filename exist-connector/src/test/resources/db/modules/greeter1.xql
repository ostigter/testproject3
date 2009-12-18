xquery version "1.0";

import module namespace request="http://exist-db.org/xquery/request"; 

declare namespace gr = "http://www.example.org/greeter";

declare function gr:greeting($name as xs:string) as element(Greeting) {
	let $greeting :=
		if ($name = "") then
			"Hello!"
		else
    		concat("Hello, ", $name, "!")
	return
		<Greeting>{$greeting}</Greeting>
};

let $name := request:get-parameter("name", "")
return
	gr:greeting($name)
