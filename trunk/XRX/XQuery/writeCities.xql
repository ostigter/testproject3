xquery version "1.0";

declare namespace xmldb = "http://exist-db.org/xquery/xmldb";

for $city in //city
    let $id := concat($city/@country, '_', replace($city/name, ' ', '_'))
    let $doc :=
        <document>
            <header>
                <id>{$id}</id>
            </header>
            <body>{
                $city
            }</body>
        </document>
    let $docName := concat($id, '.xml')
    let $dummy := xmldb:store('/db/data/cities', $docName, $doc)
    return concat('Stored document ', $docName)
