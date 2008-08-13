
module namespace gr = "http://www.example.com/greeting";

declare function gr:greeting() as xs:string {
  "Hello!"
};

declare function gr:greeting($name as xs:string) as xs:string {
  concat("Hello, ", $name, "!")
};
