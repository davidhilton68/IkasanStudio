package org.ikasan.studio.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ikasan.studio.StudioException;
import org.ikasan.studio.model.ikasan.meta.IkasanMeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ComponentDeserialisation {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
    public static IkasanMeta deserializeComponent(final String path) throws StudioException {

        final InputStream inputStream = ComponentDeserialisation.class.getClassLoader().getResourceAsStream(path);
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
}
