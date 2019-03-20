package com.example;

import com.example.model.Person;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;


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


    @Bean
    private DataFormat gsonDataFormat() {
        GsonDataFormat dataFormat = new GsonDataFormat();
        dataFormat.setUnmarshalType(Person.class);
        return dataFormat;
    }

    @Bean
    private DataFormat jackson() {

        //dataFormat.disableFeature();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//        mapper.setDateFormat(new SimpleDateFormat("yyyyMMdd"));
        // JacksonDataFormat dataFormat = new JacksonDataFormat(mapper, Person.class);
        JacksonDataFormat dataFormat = new JacksonDataFormat(Person.class);
        return dataFormat;
    }

    @Override
    public void configure() {
        getContext().setStreamCaching(true);
        //default logging error
        //getContext().setErrorHandlerBuilder(deadLetterChannel("direct:defaultErrorHandler"));
        restConfiguration().component("restlet").port(9090);
        rest().get().to("direct:returnBody");
        rest().post().to("direct:transformJson");

        from("file:json?noop=true")
                //.unmarshal("gsonDataFormat")
                .unmarshal("jackson")
                .log("Date: ${body.date}")
                .end();


//
//        from("direct:defaultErrorHandler").routeId("errorHandler")
//                .log(LoggingLevel.ERROR, "${exception.message}")
//                .transform(simple("${exception.message} ${messageHistory}"))
//                .to("freemarker:templates/error.ftl");
//
//        from("direct:returnBody").routeId("routeGet")
//                .setHeader("content-type", constant("text/html"))
//                .setHeader("message", simple("{{message}}"))
//                .to("freemarker:templates/response.ftl");
//
//        from("direct:transformJson").routeId("routePost")
//                .setHeader("content-type", constant("text/xml"))
//                .unmarshal("xmljson");
//
//        from("file:data?noop=true")
//                .to("schematron:validators/schematron.sch")
//                .log("${headers.CamelSchematronValidationStatus}")
//                .log("${headers.CamelSchematronValidationReport}");

    }

}
