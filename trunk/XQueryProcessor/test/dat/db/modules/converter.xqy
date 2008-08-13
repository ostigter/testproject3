module namespace cnv = "http://www.example.com/converter";

declare function cnv:convert(
    $doc1 as document-node(),
    $doc2 as document-node(),
    $options as xs:string*
) as document-node() {
    (: Get data from first input document :)
    let $inputValue1 := xs:integer($doc1/Document/Value)

    (: Get data from second input document :)
    let $inputValue2 := xs:integer($doc2/Document/Value)

    (: Combine the data :)
    let $mergedValue :=
        if ($options[1] eq "multiply") then
            $inputValue1 * $inputValue2
        else
            $inputValue1 + $inputValue2

    (: Build and return a new, combined document :)
    return
    document {
        <Document>
            <Doc1>{$doc1}</Doc1>
            <Doc2>{$doc2}</Doc2>
            <Options>{
                for $op in $options
                    return <Option>{$op}</Option>
            }</Options>
            <MergedValue>{$mergedValue}</MergedValue>
        </Document>
    }
};
