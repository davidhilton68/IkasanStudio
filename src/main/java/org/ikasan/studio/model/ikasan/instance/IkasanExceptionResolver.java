package org.ikasan.studio.model.ikasan.instance;

import org.ikasan.studio.model.ikasan.meta.IkasanComponentLibrary;
import org.ikasan.studio.ui.viewmodel.ViewHandlerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulate the IkasanExceptionResolver.
 */

public class IkasanExceptionResolver extends FlowElement {
    //@todo need to split IkasanComponent to nest IkasanExceptionResolver above a protected Map<IkasanComponentPropertyMetaKey, IkasanComponentProperty> configuredProperties; level
    private Map<String, IkasanExceptionResolution> ikasanExceptionResolutionMap = new HashMap<>();  // list of all the exceptions we catch / process

    /**
     * Create an IkasanExceptionResolver
     * @param parent flow that contains this exceptions resolver
     */
    public IkasanExceptionResolver(Flow parent) {
        super(IkasanComponentLibrary.getExceptionResolver(IkasanComponentLibrary.STD_IKASAN_PACK), parent);
        this.viewHandler = ViewHandlerFactory.getInstance(this);
    }

    public Map<String, IkasanExceptionResolution> getIkasanExceptionResolutionMap() {
        return ikasanExceptionResolutionMap;
    }

    public void addExceptionResolution(IkasanExceptionResolution ikasanExceptionResolution) {
        ikasanExceptionResolutionMap.put(ikasanExceptionResolution.getExceptionsCaught(), ikasanExceptionResolution);
    }

    public void resetIkasanExceptionResolutionList() {
        ikasanExceptionResolutionMap = new HashMap<>();
    }

    /**
     * Determine if the Resolver is valid i.e. has more than one resolution.
     * @return true if the Resolver is valid.
     */
    public boolean isValid() {
        return (ikasanExceptionResolutionMap != null && !ikasanExceptionResolutionMap.isEmpty());
    }
}
