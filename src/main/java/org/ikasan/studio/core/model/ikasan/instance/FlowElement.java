package org.ikasan.studio.core.model.ikasan.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ikasan.studio.core.model.ikasan.instance.serialization.FlowElementSerializer;
import org.ikasan.studio.core.model.ikasan.meta.ComponentMeta;
import org.ikasan.studio.core.model.ikasan.meta.ComponentPropertyMeta;

/**
 * The component that resides in a flow e.g. broker, splitter, consumer, producer
 */
@JsonSerialize(using = FlowElementSerializer.class)
//@JsonSerialize()
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class FlowElement extends BasicElement {
    @JsonIgnore
    private Flow containingFlow;

    public FlowElement() {}

    @Builder (builderMethodName = "flowElementBuilder")
    protected FlowElement(ComponentMeta componentMeta, Flow containingFlow, String componentName) {
        super(componentMeta, null);
        setPropertyValue(ComponentPropertyMeta.COMPONENT_NAME, componentName);
        this.containingFlow = containingFlow;
    }

    @Override
    public String toString() {
        return "IkasanFlowComponent {" +
                ", flowComponent='" + getComponentMeta() + '\'' +
                ", name='" + getComponentName() + '\'' +
                ", properties=" + configuredProperties +
                '}';
    }
}
