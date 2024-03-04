package org.ikasan.studio.model.ikasan.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.ikasan.studio.model.ikasan.meta.ComponentPropertyMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the value of a property
 */
public class ComponentProperty {
    private Object value;
    @JsonIgnore
    private ComponentPropertyMeta meta;

    public ComponentProperty(ComponentPropertyMeta meta, Object value) {
        this.meta = meta;
        this.value = value;
    }

    public ComponentProperty(ComponentPropertyMeta meta) {
        this(meta, null);
    }

    public Object getValue() {
        return value;
    }

    @JsonIgnore
    public Object getDefaultValue() {
        return meta.getDefaultValue();
    }

//    /**
//     * Get the value and present it in such a way as to be appropriate for display in the template language
//     * @return a string that contains the value display in such a way as to be appropriate for inclusion in a template
//     */
//    @JsonIgnore
//    public String getTemplateRepresentationOfValue() {
//        String displayValue = "";
//        if (value == null) {
//            displayValue = null;
//        } else if (meta != null) {
//            if ("java.lang.String".equals(meta.getUsageDataType())) {
//                displayValue = StudioPsiUtils.stripStartAndEndQuotes((String)value);
//                displayValue = "\"" + displayValue + "\"";
//            } else {
//                displayValue = value.toString();
//            }
//        }
//        return displayValue;
//    }

    @JsonIgnore
    public String getValueString() {
        return value == null ? "null" : value.toString();
    }

    public void setValue(Object value) {
        this.value = value;
    }

//    /**
//     * Using the supplied string and meta data, attempt to set the value to an object of the appropriate class in accordance
//     * with the metaData
//     * @param newValue to update
//     */
//    public void setValueFromString(String newValue) {
//        if (meta != null) {
//            if (meta.getPropertyDataType() == String.class) {
//                value = StudioPsiUtils.stripStartAndEndQuotes(newValue);
//            } else {
//                try {
//                    if (meta.getPropertyDataType() == Long.class) {
//                        value = Long.parseLong(newValue);
//                    } else if (meta.getPropertyDataType() == Integer.class) {
//                        value = Integer.parseInt(newValue);
//                    } else if (meta.getPropertyDataType() == Double.class) {
//                        value = Double.parseDouble(newValue);
//                    } else if (meta.getPropertyDataType() == Float.class) {
//                        value = Float.parseFloat(newValue);
//                    }
//                } catch (NumberFormatException nfe) {
//                    LOG.warn("Failed to set value of type " + meta.getPropertyDataType() + " to value " + newValue);
//                }
//            }
//        }
//    }

    @JsonIgnore
    public ComponentPropertyMeta getMeta() {
        return meta;
    }

    public void setMeta(ComponentPropertyMeta meta) {
        this.meta = meta;
    }

    public boolean affectsBespokeClass() {
        return Boolean.TRUE.equals(getMeta().isAffectsBespokeClass());
    }

    @Override
    public String toString() {
        return "ComponentProperty{" +
                "value=" + value +
                ", meta=" + meta +
                '}';
    }

    /**
     * For the given field type, determine if a valid value has been set.
     * @return true if the field is empty or unset
     */
    @JsonIgnore
    public boolean valueNotSet() {
        return (value == null) ||
                (value instanceof String && ((String) value).isEmpty()) ||
                (value instanceof Integer && ((Integer) value) == 0) ||
                (value instanceof Long && ((Long) value) == 0) ||
                (value instanceof Double && ((Double) value) == 0.0) ||
                (value instanceof Float && ((Float) value) == 0.0);
    }

    /**
     * For the given property list of meta, create a list of (instance) properties that correspond to it
     * @param metaList of properties suitable for this component
     * @return a list of properties that this instance could poses
     */
    public static List<ComponentProperty> generateIkasanComponentPropertyList(List<ComponentPropertyMeta> metaList) {
        List<ComponentProperty> propertyList = new ArrayList<>();
        if (metaList != null && !metaList.isEmpty()) {
            for(ComponentPropertyMeta meta : metaList) {
                propertyList.add(new ComponentProperty(meta));
            }
        }
        return propertyList;
    }
}
