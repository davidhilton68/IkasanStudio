package org.ikasan.studio.model.Ikasan;

import org.ikasan.studio.ui.viewmodel.IkasanFlowComponentViewHandler;

public class IkasanFlowComponent extends IkasanComponent {
    private IkasanFlow parent;
    private IkasanFlowComponentType type;

    private IkasanFlowComponent() {}

    /**
     * Any component that belongs in the flow
     * @param type e.g. EVENT_DRIVEN_CONSUMER, PAYLOAD_TO_MAP_CONVERTER
     * @param parent flow that contains this element
     * @param name of the element
     * @param description of the element
     */
    protected IkasanFlowComponent(IkasanFlowComponentType type, IkasanFlow parent, String name, String description) {
        super ();
        this.type = type;
        this.parent = parent;
        this.properties = type.getMandatoryProperties();
        setPropertyValue(IkasanComponentPropertyMeta.NAME, name);
        setPropertyValue(IkasanComponentPropertyMeta.DESCRIPTION, description);
        viewHandler = new IkasanFlowComponentViewHandler(this);
    }

    /**
     * Any component that belongs in the flow
     * @param type e.g. EVENT_DRIVEN_CONSUMER, PAYLOAD_TO_MAP_CONVERTER
     * @param parent flow that contains this element
     */
    protected IkasanFlowComponent(IkasanFlowComponentType type, IkasanFlow parent) {
        this(type, parent, "", "");
    }

    /**
     * Any component that belongs in the flow
     * @param type e.g. EVENT_DRIVEN_CONSUMER, PAYLOAD_TO_MAP_CONVERTER
     * @param parent flow that contains this element
     * @param name of the element
     * @param description of the element
     */
    public static IkasanFlowComponent getInstance(IkasanFlowComponentType type, IkasanFlow parent, String name, String description) {
        if (type.isBespokeClass()) {
            return new IkasanFlowBeskpokeComponent(type, parent, name, description, false);
        } else {
            return new IkasanFlowComponent(type, parent, name, description);
        }
    }

    /**
     * Any component that belongs in the flow
     * @param type e.g. EVENT_DRIVEN_CONSUMER, PAYLOAD_TO_MAP_CONVERTER
     * @param parent flow that contains this element
     */
    public static IkasanFlowComponent getInstance(IkasanFlowComponentType type, IkasanFlow parent) {
        if (type.isBespokeClass()) {
            return new IkasanFlowBeskpokeComponent(type, parent, false);
        } else {
            return new IkasanFlowComponent(type, parent);
        }
    }

    public IkasanFlow getParent() {
        return parent;
    }

    public IkasanFlowComponentType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "IkasanFlowComponent{" +
                ", name='" + getName() + '\'' +
                ", type=" + type +
                ", properties=" + properties +
                '}';
    }
}
