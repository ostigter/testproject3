xquery version "1.0";

module namespace gr = "http://www.example.org/greeter";

declare function gr:greeting($name as xs:string) as element(Greeting) {
	let $greeting :=
		if ($name = "") then
			"Hello!"
		else
    		concat("Hello, ", $name, "!")
	return
		<Greeting>{$greeting}</Greeting>
};
