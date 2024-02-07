package org.ikasan.studio.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.diagnostic.Logger;
import org.ikasan.studio.StudioException;
import org.ikasan.studio.model.ikasan.instance.Module;
import org.ikasan.studio.model.ikasan.meta.IkasanMeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ComponentIO {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = Logger.getInstance("#ComponentIO");

    static {
        MAPPER
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /*
     * Deserialize the component at the given path.
     * The component could be any of the child classes of IkasanMeta
     * @param path to the file containing the JSON to deserializes
     * @return an IkasanMeta object representing the deserialized class
     * @throws StudioException  to wrap JsonProcessingException
     */
    public static IkasanMeta deserializeMetaComponent(final String path) throws StudioException {

        final InputStream inputStream = ComponentIO.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new StudioException("The serialised data in [" + path + "] could not be loaded, check the path is correct");
        }
        final String jsonString = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());

        IkasanMeta ikasanMeta;
        try {
            ikasanMeta = MAPPER.readValue(jsonString, IkasanMeta.class);
        } catch (JsonProcessingException e) {
            throw new StudioException("The serialised data in [" + path + "] could not be read due to" + e.getMessage(), e);
        }
        return ikasanMeta;
    }

    /*
     * Deserialize the component at the given path.
     * The component could be any of the child classes of IkasanBaseElement
     * @param path to the file containing the JSON to deserializes
     * @return an IkasanBaseElement object representing the deserialized class
     * @throws StudioException  to wrap JsonProcessingException
     */
//    public static IkasanBaseElement deserializeInstanceComponent(final String path, IkasanBaseElement objectToCreate) throws StudioException {
//
//        final InputStream inputStream = ComponentIO.class.getClassLoader().getResourceAsStream(path);
//        if (inputStream == null) {
//            throw new StudioException("The serialised data in [" + path + "] could not be loaded, check the path is correct");
//        }
//        final String jsonString = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
//                .lines()
//                .collect(Collectors.joining());
//
////        IkasanBaseElement ikasanBaseElement;
//        Object ikasanBaseElement;
//        try {
////            ikasanBaseElement = objectToCreate.callBack(MAPPER, jsonString);
//            ikasanBaseElement = MAPPER.readValue(jsonString, org.ikasan.studio.model.ikasan.instance.Module.class);
//        } catch (JsonProcessingException e) {
//            throw new StudioException("The serialised data in [" + path + "] could not be read due to" + e.getMessage(), e);
//        }
//        return (IkasanBaseElement)ikasanBaseElement;
//    }

    /*
     * Deserialize the component at the given path.
     * The component could be any of the child classes of IkasanBaseElement
     * @param path to the file containing the JSON to deserializes
     * @return an IkasanBaseElement object representing the deserialized class
     * @throws StudioException  to wrap JsonProcessingException
     */
    public static Module deserializeModuleInstance(final String path) throws StudioException {

        final InputStream inputStream = ComponentIO.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new StudioException("The serialised data in [" + path + "] could not be loaded, check the path is correct");
        }
        final String jsonString = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());

        Module moduleInstanc;
        try {
            moduleInstanc = MAPPER.readValue(jsonString, Module.class);
        } catch (JsonProcessingException e) {
            throw new StudioException("The serialised data in [" + path + "] could not be read due to" + e.getMessage(), e);
        }
        return moduleInstanc;
    }


    /**
     * Convert the supplied java Object into its JSON representation
     * @param value to be turned to JSON
     * @return the Object in JSON format.
     */
    public static String toJson(Object value) {
        String moduleString = "CouldNotConvert";

        try {
            moduleString = MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException jpe) {
            value = "";
            LOG.warn("Could not generate JSON from [" + value + "] message [" + jpe.getMessage() + "]", jpe);
        }
        return moduleString;
    }
}
