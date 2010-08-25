xquery version "1.0";

module namespace xf = "http://www.example.org/xforms";

declare function xf:form($title as xs:string) {
    let $form :=
        <html xmlns="http://www.w3.org/1999/xhtml"
                xmlns:xf="http://www.w3.org/2002/xforms"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:ev="http://www.w3.org/2001/xml-events">
            <head>
                <title>{$title}</title>
                <xf:model>
                    <xf:instance>
                        <data>
                            <Message>XForms is working perfectly!</Message>
                        </data>
                    </xf:instance>
                </xf:model>
            </head>
            <body>
                <h2><xf:output ref="Message" /></h2>
            </body>
        </html>
    let $xslt-pi := processing-instruction xml-stylesheet {'type="text/xsl" href="/exist/xforms/xsltforms/xsltforms.xsl"'}
    return ($xslt-pi, $form)
};
