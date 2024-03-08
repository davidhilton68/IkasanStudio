package org.ikasan.studio.model.ikasan.instance.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.ikasan.studio.model.ikasan.instance.FlowElement;

import java.io.IOException;

import static org.ikasan.studio.model.ikasan.meta.ComponentMeta.*;

public class FlowElementSerializer extends StdSerializer<FlowElement> {

    public FlowElementSerializer() {
        super(FlowElement.class);
    }

    @Override
    public void serialize(FlowElement flowElement, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        serializePayload(flowElement, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    protected void serializePayload(FlowElement flowElement, JsonGenerator jsonGenerator) throws IOException {
        BasicElementSerializer basicElementSerializer = new BasicElementSerializer();
        basicElementSerializer.serializePayload(flowElement, jsonGenerator);

        // This is metadata but used to identify the element
        jsonGenerator.writeStringField(COMPONENT_TYPE, flowElement.getComponentMeta().getComponentType());
        jsonGenerator.writeStringField(IMPLEMENTING_CLASS, flowElement.getComponentMeta().getImplementingClass());
        if (flowElement.getComponentMeta().getAdditionalKey() != null) {
            jsonGenerator.writeStringField(ADDITIONAL_KEY, flowElement.getComponentMeta().getAdditionalKey());
        }
    }
}
