package com.zhy.test.utils;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class JsonAndXmlUtil {

    private static final Logger logger =  LoggerFactory.getLogger(JsonAndXmlUtil.class);

    public static String xmlToJson(String xml){
        StringReader input = new StringReader(xml);
        StringWriter output = new StringWriter();
        JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true).autoPrimitive(true).prettyPrint(true).build();

        try {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD,Boolean.FALSE);
        XMLEventReader reader = factory.createXMLEventReader(input);
        XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);
        writer.add(reader);
        reader.close();
        writer.close();
        } catch (XMLStreamException e) {
            logger.info(e.getMessage(),e);
        }finally {
            try {
                output.close();
                input.close();
            } catch (IOException e) {
                logger.info(e.getMessage(),e);
            }
        }

        return output.toString();
    }


}
