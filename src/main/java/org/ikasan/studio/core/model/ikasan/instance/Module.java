package org.ikasan.studio.core.model.ikasan.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.intellij.openapi.diagnostic.Logger;
import lombok.*;
import org.apache.maven.model.Dependency;
import org.ikasan.studio.core.StudioBuildException;
import org.ikasan.studio.core.model.ModelUtils;
import org.ikasan.studio.core.model.ikasan.instance.serialization.ModuleDeserializer;
import org.ikasan.studio.core.model.ikasan.instance.serialization.ModuleSerializer;
import org.ikasan.studio.core.model.ikasan.meta.ComponentPropertyMeta;
import org.ikasan.studio.core.model.ikasan.meta.IkasanComponentLibrary;

import java.util.*;

/**
 * This class holds all the information about the ikasan module flow.
 * It's a deliberate decision not to use components from within the ikasan framework itself in an attempt to insulate
 * from any changes to ikasan or componentDependencies on any particular ikasan version.
 */

@Getter
@Setter
@ToString
//@AllArgsConstructor
@JsonSerialize(using = ModuleSerializer.class)
@JsonDeserialize(using = ModuleDeserializer.class)
public class Module extends BasicElement {
    public static final Logger LOG = Logger.getInstance("Module");
    public static final String DUMB_MODULE_VERSION = "DUMB_MODULE";     // Allows creation of emergency modelto cope with crash scenarios

    @JsonPropertyOrder(alphabetic = true)
    @JsonSetter(nulls = Nulls.SKIP)   // If the supplied value is null, ignore it.
    private List<Flow> flows;

    public Module(String metaPackVersion) throws StudioBuildException {
        super (IkasanComponentLibrary.getModuleComponentMetaMandatory(metaPackVersion), null);
        flows = new ArrayList<>();
    }

    @Builder(builderMethodName = "moduleBuilder")
    public Module(
            String name,
            String description,
            @NonNull
            String version,
            String applicationPackageName,
            String port,
            String h2PortNumber,
            String h2WebPortNumber,
            List<Flow> flows) throws StudioBuildException {
        super (IkasanComponentLibrary.getModuleComponentMetaMandatory(version), description);

        setVersion(version);
        setName(name);
        setApplicationPackageName(applicationPackageName);
        setPort(port);
        setH2DbPortNumber(h2PortNumber);
        setH2WebPortNumber(h2WebPortNumber);
        this.flows = Objects.requireNonNullElseGet(flows, ArrayList::new);
    }

    /**
     * Used to determine whether the module has been initialised or needs to be initialised
     * @return true if all the mandatory fields have values
     */
    @JsonIgnore
    public boolean isInitialised() {
        return (
                getName() != null && !getName().isBlank() &&
                getVersion() != null && !getVersion().isBlank() &&
                getApplicationPackageName() != null && !getApplicationPackageName().isBlank() &&
                getPort() != null && !getPort().isBlank() &&
                getH2PortNumber() != null && !getH2PortNumber().isBlank() &&
                getH2WebPortNumber() != null && !getH2WebPortNumber().isBlank()
        );
    }

    private Module() {}
    public static Module getDumbModuleVersion() {
        Module module = new Module();
        if (module.getComponentProperties() == null) {
            module.setComponentProperties(new TreeMap<>());
        }
        module.getComponentProperties().put(ComponentPropertyMeta.VERSION, new ComponentProperty(ComponentPropertyMeta.DUMB_VERSION, DUMB_MODULE_VERSION));
//        module.setVersion(DUMB_MODULE);
        module.setFlows(new ArrayList<>());
        return module;
    }

    public boolean addFlow(Flow ikasanFlow) {
        return flows.add(ikasanFlow);
    }

    public String getApplicationPackageName() {
        return (String) getPropertyValue(ComponentPropertyMeta.APPLICATION_PACKAGE_NAME);
    }
    @JsonIgnore
    public void setApplicationPackageName(String applicationPackageName) {
        this.setPropertyValue(ComponentPropertyMeta.APPLICATION_PACKAGE_NAME, applicationPackageName);
    }
    public String getPort() {
        return (String) getPropertyValue(ComponentPropertyMeta.APPLICATION_PORT_NUMBER_NAME);
    }
    @JsonIgnore
    public void setPort(String portNumber) {
        this.setPropertyValue(ComponentPropertyMeta.APPLICATION_PORT_NUMBER_NAME, portNumber);
    }
    public String getH2PortNumber() {
        return (String) getPropertyValue(ComponentPropertyMeta.H2_DB_PORT_NUMBER_NAME);
    }
    @JsonIgnore
    public void setH2DbPortNumber(String portNumber) {
        this.setPropertyValue(ComponentPropertyMeta.H2_DB_PORT_NUMBER_NAME, portNumber);
    }
    public String getH2WebPortNumber() {
        return (String) getPropertyValue(ComponentPropertyMeta.H2_WEB_PORT_NUMBER_NAME);
    }
    @JsonIgnore
    public void setH2WebPortNumber(String portNumber) {
        this.setPropertyValue(ComponentPropertyMeta.H2_WEB_PORT_NUMBER_NAME, portNumber);
    }

    @JsonIgnore
    public String getMetaVersion() {
        String version = (String) getPropertyValue(ComponentPropertyMeta.VERSION);
        if (version == null) {
            LOG.error("SERIOUS ERROR - to getMetaVersion but it was null");
        }
        return version;
    }

    /**
     * Get the sorted set of all Jar dependencies for the module and all its components
     * The set will be sorted on groupId, artifactID and version.
     * The set will be de-duplicated based on groupId, artifactID and version.
     * @return a set of jar Dependencies for the module
     */
    public Set<Dependency> getAllUniqueSortedJarDependencies() {
        Set<Dependency> allJarDepedencies = new HashSet<>(this.getComponentMeta().getJarDependencies());

         for(Flow flow : this.getFlows()) {
             for (FlowElement flowElement : flow.getFlowRoute().getConsumerAndFlowRouteElements()) {
                 if (flowElement.getComponentMeta().getJarDependencies() != null) {
                     allJarDepedencies.addAll(flowElement.getComponentMeta().getJarDependencies());
                 }
             }
        }
        return ModelUtils.getAllUniqueSortedDependenciesSet(allJarDepedencies);
    }

    @Override
    public String toSimpleString() {
        StringBuilder builder = new StringBuilder();
        if (flows != null && !flows.isEmpty()) {
            for(Flow flow : flows) {
                builder.append(flow.toSimpleString()).append(", \n");
            }
        }
        return "Module{" +
                super.toSimpleString() + "\n" +
                "AllFlows [" + builder + "]" +
                '}';
    }
}
