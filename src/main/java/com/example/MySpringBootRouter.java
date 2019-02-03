package com.example;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.spi.DataFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    @Bean
    private DataFormat xmljson() {
        XmlJsonDataFormat dataFormat = new XmlJsonDataFormat();
        dataFormat.setRootName("root");
        dataFormat.setElementName("element");
        return dataFormat;
    }

    @Override
    public void configure() {
        getContext().setStreamCaching(true);
        //default logging error
        getContext().setErrorHandlerBuilder(deadLetterChannel("direct:defaultErrorHandler"));
        restConfiguration().component("restlet").port(9090);
        rest().get().to("direct:returnBody");
        rest().post().to("direct:transformJson");

        from("direct:defaultErrorHandler").routeId("errorHandler")
                .log(LoggingLevel.ERROR,"${exception.message}")
                .transform(simple("${exception.message} ${messageHistory}"))
                .to("freemarker:templates/error.ftl");

        from("direct:returnBody").routeId("routeGet")
                .setHeader("content-type", constant("text/html"))
                .setHeader("message", simple("{{message}}"))
                .to("freemarker:templates/response.ftl");

        from("direct:transformJson").routeId("routePost")
                .setHeader("content-type", constant("text/xml"))
                .unmarshal("xmljson");

    }

}
