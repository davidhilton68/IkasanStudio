package org.ikasan.studio.core.model.ikasan.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.ikasan.studio.core.StudioBuildException;
import org.ikasan.studio.core.model.ikasan.instance.serialization.FlowSerializer;
import org.ikasan.studio.core.model.ikasan.meta.IkasanComponentLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@JsonSerialize(using = FlowSerializer.class)
public class Flow extends BasicElement {
    private static final Logger LOG = LoggerFactory.getLogger(BasicElement.class);
    // The fields of a Flow will need to be known for serialisation
    public static final String CONSUMER_JSON_TAG = "consumer";
    public static final String TRANSITIONS_JSON_TAG = "transitions";
    public static final String FLOW_ELEMENTS_JSON_TAG = "flowElements";
    public static final String EXCEPTION_RESOLVER_JSON_TAG = "exceptionResolver";

    private FlowElement consumer;
    private FlowRoute flowRoute;
    private ExceptionResolver exceptionResolver;

    /**
     * Used primarily during deserialization.
     */
    public Flow() throws StudioBuildException {
        super (IkasanComponentLibrary.getFLowComponentMeta(IkasanComponentLibrary.DEFAULT_IKASAN_PACK), null);
        LOG.error("Parameterless version of flow called");
    }

    public Flow(String metapackVersion) throws StudioBuildException {
        super (IkasanComponentLibrary.getFLowComponentMeta(metapackVersion), null);
        flowRoute = FlowRoute.flowBuilder().flow(this).build();
    }

    @Builder(builderMethodName = "flowBuilder")
    public Flow(
                @NonNull
                String metapackVersion,
                FlowElement consumer,
                FlowRoute flowRoute,
                ExceptionResolver exceptionResolver,
                String name,
                String description) throws StudioBuildException {
        super(IkasanComponentLibrary.getFLowComponentMeta(metapackVersion), null);
        if (consumer != null) {
            if (!consumer.getComponentMeta().isConsumer()) {
                LOG.error("ERROR : Tried to set consumer on " + this + " with a flowElement that is not a consumer " + consumer + ", this will be ignored");
            } else {
                this.consumer = consumer;
            }
        }
        if (flowRoute != null) {
            this.flowRoute = flowRoute;
        } else {
            this.flowRoute = FlowRoute.flowBuilder().flow(this).build();
        }
        this.exceptionResolver = exceptionResolver;
        super.setName(name);
        super.setDescription(description);
    }

    public void removeFlowElement(FlowElement ikasanFlowComponentToBeRemoved) {
        if (ikasanFlowComponentToBeRemoved != null) {
            if (ikasanFlowComponentToBeRemoved.getComponentMeta().isConsumer()) {
                setConsumer(null);
            } else if (ikasanFlowComponentToBeRemoved.getComponentMeta().isExceptionResolver()) {
                setExceptionResolver(null);
            } else if (flowRoute != null) {
                flowRoute.removeFlowElement(ikasanFlowComponentToBeRemoved);
            } else {
                LOG.warn("Attempt to remove element " + ikasanFlowComponentToBeRemoved + " because it could not be found in the memory model");
            }
        }
    }

    public boolean hasConsumer() {
        return getConsumer() != null;
    }

    /**
     * Does the Flow have a valid exception resolver
     * @return if the flow has a valid exception resolver.
     */
    public boolean hasExceptionResolver() {
        return (exceptionResolver != null);
    }

    public boolean anyFlowRouteHasComponents(FlowRoute flowRoute) {
        if (flowRoute != null) {
            if (flowRoute.getFlowElements() != null && !flowRoute.getFlowElements().isEmpty()) {
                return true;
            }
        }
        if (flowRoute.getChildRoutes() != null) {
            for(FlowRoute childRoute : flowRoute.getChildRoutes()) {
                if (anyFlowRouteHasComponents(childRoute)) {
                    return true;
                }
            }
        }
        return false;
    }

    public FlowRoute getFlowRouteContaining(FlowRoute searchRoute, FlowElement targetFlowElement) {
        if (searchRoute != null) {
            if (searchRoute.getFlowElements() != null && !searchRoute.getFlowElements().isEmpty() && searchRoute.getFlowElements().contains(targetFlowElement)) {
                return searchRoute;
            }
        }
        if (searchRoute.getChildRoutes() != null) {
            for(FlowRoute childRoute : searchRoute.getChildRoutes()) {
                if (getFlowRouteContaining(childRoute, targetFlowElement) != null) {
                    return childRoute;
                }
            }
        }
        return null;
    }

    public List<FlowRoute> getAllFlowRoutes(List<FlowRoute> flowRoutes, FlowRoute currentRoot) {
        if (currentRoot != null) {
            flowRoutes.add(currentRoot);
        }
        if (currentRoot.getChildRoutes() != null && !currentRoot.getChildRoutes().isEmpty()) {
            for(FlowRoute childRoute : currentRoot.getChildRoutes()) {
                getAllFlowRoutes(flowRoutes, childRoute);
            }
        }
        return flowRoutes;
    }


    /**
     * This method is used by FreeMarker, the IDE may incorrectly identify it as unused.
     * @return A list of all non-null flow elements, including the consumer
     */
    public List<FlowElement> ftlGetConsumerAndFlowElements() {

        List<FlowElement> allElements = new ArrayList<>();
        // Only the default (primary) flowRoute includes the consumer
        if (getConsumer() != null) {
            allElements.add(getConsumer());
        }
        getAllFlowElementsInAnyRoute(allElements, getFlowRoute());
        return allElements;
    }

    public List<FlowElement> getAllFlowElementsInAnyRoute(List<FlowElement> flowElementsList, FlowRoute flowRoute) {
        if (flowRoute != null) {
            List<FlowElement> thisRouteFlowElementList = flowRoute.getFlowElements();
            if (thisRouteFlowElementList != null && !thisRouteFlowElementList.isEmpty()) {
                flowElementsList.addAll(thisRouteFlowElementList);
            }
        }
        if (flowRoute.getChildRoutes() != null) {
            for(FlowRoute childRoute : flowRoute.getChildRoutes()) {
                getAllFlowElementsInAnyRoute(flowElementsList, childRoute);
            }
        }
        return flowElementsList;
    }

    /**
     * This method is used by FreeMarker, the IDE may incorrectly identify it as unused.
     * @return A list of all non-null flow elements, including the consumer, excluding any endpoints
     */
    public List<FlowElement> ftlGetAllFlowElementsInAnyRouteNoEndpoints() {

        List<FlowElement> allElements = ftlGetConsumerAndFlowElements();
        allElements = allElements.stream()
            .filter(flowElement -> !flowElement.componentMeta.isEndpoint())
            .toList();
        return allElements;
    }

    /**
     * This method is used by FreeMarker, the IDE may incorrectly identify it as unused.
     * @return A list of all non-null flow elements, including the consumer, excluding any endpoints
     */
    public List<FlowElement> getFlowElementsNoExternalEndPoints() {

        List<FlowElement> allElements = ftlGetConsumerAndFlowElements();
        allElements = allElements.stream()
            .filter(x-> ! x.componentMeta.isEndpoint() || x.componentMeta.isInternalEndpoint())
            .toList();
        return allElements;
    }

    /**
     * Determine the current state of the flow for completeness
     * @return A status string
     */
    @JsonIgnore
    public String getFlowIntegrityStatus() {
        String status = "";
        if (! hasConsumer()) {
            status += "The flow needs a consumer";
        }
        if (flowRoute != null) {
            status += flowRoute.getFlowIntegrityStatus();
        }
        if (!status.isEmpty()) {
            status += " to be complete.";
        }
        return status;
    }
}
