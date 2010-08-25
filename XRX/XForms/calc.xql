xquery version "1.0";

declare namespace request="http://exist-db.org/xquery/request";

declare option exist:serialize "method=xml media-type=text/xml indent=yes omit-xml-declaration=no";

let $requestBody := request:get-data()
let $number1 := $requestBody//number1/text()
let $number2 := $requestBody//number2/text()
let $result  := $number1 + $number2
return
    <data xmlns="">
        <number1>{$number1}</number1>
        <number2>{$number2}</number2>
        <result>{$result}</result>
    </data>
