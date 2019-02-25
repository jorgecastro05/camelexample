<schema xmlns="http://purl.oclc.org/dsdl/schematron">
    <pattern name="Required Elements">
        <let name="MandatoryField" value="'El campo es obligatorio'"/>
        <rule context="item">
            <assert test="title/text()">
                <value-of select="$MandatoryField"/>
            </assert>
        </rule>

    </pattern>
    <pattern name="Datatypes">
        <rule context="quantity[text()]">
            <assert test="number(.)">El valor debe ser numerico</assert>
        </rule>
    </pattern>
</schema>