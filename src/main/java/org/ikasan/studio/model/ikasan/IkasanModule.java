package org.ikasan.studio.model.ikasan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.intellij.psi.PsiFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.ikasan.studio.ui.viewmodel.ViewHandlerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all the information about the ikasan module flow.
 * Its a deliberate decision not to use components from within the ikasan framework itself in an attempt to insulate
 * from any changes to ikasan or componentDependencies on any particular ikasan version.
 */
//@Data
@Data
@AllArgsConstructor
@Jacksonized
@Builder
public class IkasanModule extends IkasanComponent {
    @JsonPropertyOrder(alphabetic = true)
    @JsonIgnore
    private PsiFile moduleConfig;
    private String version;
    private List<IkasanFlow> flows = new ArrayList<>();

    public IkasanModule() {
        super(IkasanComponentType.MODULE, IkasanComponentType.MODULE.getMandatoryProperties());
        this.viewHandler = ViewHandlerFactory.getInstance(this);
    }

    /**
     * This will be called if the module is reloaded or re-initialised.
     */
    public void reset() {
        if (flows != null && !flows.isEmpty()) {
            flows = new ArrayList<>();
        }
        setName("");
        setDescription("");
    }

    /**
     * Ensure any permissions to regenerate source is reset to false
     */
    public void resetRegenratePermissions() {
        for (IkasanFlow flow : flows) {
            for (IkasanComponent component : flow.getFlowComponentList()) {
                component.resetUserImplementedClassPropertiesRegenratePermission();
            }
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<IkasanFlow> getFlows() {
        return flows;
    }

    public IkasanFlow getFlow(IkasanFlow searchedFlow) {
        if (searchedFlow != null && flows != null && !flows.isEmpty()) {
            for (IkasanFlow currentFlow : getFlows()) {
                if (searchedFlow.equals(currentFlow)) {
                    return currentFlow;
                }
            }
        }
        return null;
    }

    public boolean addFlow(IkasanFlow ikasanFlow) {
        return flows.add(ikasanFlow);
    }

    @Override
    public String toString() {
        return "IkasanModule{" +
                "moduleConfig=" + moduleConfig +
                ", version='" + version + '\'' +
                ", flows=" + flows +
                ", Module type=" + type +
                ", Module properties=" + configuredProperties +
                '}';
    }

    @JsonIgnore
    public PsiFile getModuleConfig() {
        return moduleConfig;
    }
    public void setModuleConfig(PsiFile moduleConfig) {
        this.moduleConfig = moduleConfig;
    }

    public String getApplicationPackageName() {
        return (String) getPropertyValue(IkasanComponentPropertyMeta.APPLICATION_PACKAGE_NAME);
    }
    public String getApplicationPortNumber() {
        return (String) getPropertyValue(IkasanComponentPropertyMeta.APPLICATION_PORT_NUMBER_NAME.getPropertyName());
    }
    @JsonIgnore
    public void setApplicationPackageName(String applicationPackageName) {
        this.setPropertyValue(IkasanComponentPropertyMeta.APPLICATION_PACKAGE_NAME, IkasanComponentPropertyMeta.STD_PACKAGE_NAME_META_COMPONENT, applicationPackageName);
    }
    @JsonIgnore
    public void setApplicationPortNumber(String applicationPortNumber) {
        this.setPropertyValue(IkasanComponentPropertyMeta.APPLICATION_PORT_NUMBER_NAME, IkasanComponentPropertyMeta.STD_PORT_NUMBER_META_COMPONENT, applicationPortNumber);
    }
}
