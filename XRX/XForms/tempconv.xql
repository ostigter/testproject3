xquery version "1.0";

let $form :=
    <html xmlns="http://www.w3.org/1999/xhtml"
            xmlns:xf="http://www.w3.org/2002/xforms"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            xmlns:ev="http://www.w3.org/2001/xml-events">
        <head>
            <title>XForms Temperature Converter</title>
            <xf:model>
                <xf:instance>
                    <data xmlns="">
                        <c>20</c>
                        <f/>
                    </data>
                </xf:instance>
            </xf:model>
        </head>
        <body>
            <h1>XForms Temperature Converter</h1>
            <p>
                Enter a value, then click the appropriate button to convert between units:
            </p>
            <p>
                <xf:input ref="c">
                    <xf:label>Degrees Celcius (&#176;C): </xf:label>
                </xf:input>
            </p>
            <p>
                <xf:input ref="f">
                    <xf:label>Degrees Fahrenheit (&#176;F): </xf:label>
                </xf:input>
            </p>
            <p>
                <xf:trigger>
                    <xf:label>&#176;C &#8594; &#176;F</xf:label>
                    <xf:action ev:event="DOMActivate">
                        <xf:setvalue ref="f" value="number(/data/c) * 9 div 5 + 32"/>
                    </xf:action>
                </xf:trigger>
                <xf:trigger>
                    <xf:label>&#176;F &#8594; &#176;C</xf:label>
                    <xf:action ev:event="DOMActivate">
                        <xf:setvalue ref="c" value="( number(/data/f) - 32 ) * 5 div 9"/>
                    </xf:action>
                </xf:trigger>
            </p>
        </body>
    </html>
let $xslt-pi := processing-instruction xml-stylesheet {'type="text/xsl" href="/exist/xforms/xsltforms/xsltforms.xsl"'}
return ($xslt-pi, $form)
