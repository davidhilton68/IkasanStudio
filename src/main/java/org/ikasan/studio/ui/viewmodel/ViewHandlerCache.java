package org.ikasan.studio.ui.viewmodel;

import com.intellij.openapi.diagnostic.Logger;
import org.ikasan.studio.StudioException;
import org.ikasan.studio.core.model.ikasan.instance.*;
import org.ikasan.studio.core.model.ikasan.instance.Module;
import org.ikasan.studio.ui.UiContext;
import org.ikasan.studio.ui.model.PaletteItem;

import java.util.Arrays;

public class ViewHandlerCache implements ViewHandlerFactory {
    private static final Logger LOG = Logger.getInstance("#ViewHandlerCache");
    private final String projectKey;

    public ViewHandlerCache(String projectKey) {
        this.projectKey = projectKey;
    }

    public AbstractViewHandler getInstance(Object component) {
        AbstractViewHandlerIntellij returnAbstractViewHandlerIntellij = null ;
        if (component != null) {
            if (component instanceof PaletteItem) {
                // PaletteItems get a view handler when instantiated.
                returnAbstractViewHandlerIntellij =  ((PaletteItem) component).getIkasanPaletteElementViewHandler();
            } else if (component instanceof Module) {
                returnAbstractViewHandlerIntellij =  new IkasanModuleViewHandler(projectKey, (Module) component);
            } else if (component instanceof Flow) {
                returnAbstractViewHandlerIntellij =  new IkasanFlowViewHandler(projectKey, (Flow) component);
            } else if (component instanceof ExceptionResolver) {
                returnAbstractViewHandlerIntellij =  new IkasanFlowExceptionResolverViewHandler((ExceptionResolver) component);
            } else if (component instanceof FlowElement) {
                returnAbstractViewHandlerIntellij =  new IkasanFlowComponentViewHandler((FlowElement) component);
            }
        }
        if (returnAbstractViewHandlerIntellij == null) {
            LOG.error("STUDIO: View handler returned null for component " + component);
        }
        return returnAbstractViewHandlerIntellij;
    }

    public static IkasanFlowViewHandler getFlowViewHandler(String projectKey, Flow flow) {
        ViewHandlerCache viewHandlerCache = UiContext.getViewHandlerFactory(projectKey);
        IkasanFlowViewHandler viewHandler = null;
        try {
            viewHandler = ((IkasanFlowViewHandler)flow.getOrCreateViewHandler(viewHandlerCache));
        } catch (StudioException se) {
            LOG.warn("STUDIO: A studio exception was raised while trying to get the view handlers, please investigate: " + se.getMessage() + " Trace: " + Arrays.asList(se.getStackTrace()));
        }
        return viewHandler;
    }

    public static IkasanFlowComponentViewHandler getFlowComponentViewHandler(String projectKey, IkasanComponent ikasanComponent) {
        ViewHandlerCache viewHandlerCache = UiContext.getViewHandlerFactory(projectKey);
        IkasanFlowComponentViewHandler viewHandler = null;
        BasicElement ikasanBasicElement = (BasicElement)ikasanComponent;
        try {
            viewHandler = ((IkasanFlowComponentViewHandler)ikasanBasicElement.getOrCreateViewHandler(viewHandlerCache));
        } catch (StudioException se) {
            LOG.warn("STUDIO: A studio exception was raised while trying to get the view handlers, please investigate: " + se.getMessage() + " Trace: " + Arrays.asList(se.getStackTrace()));
        }
        return viewHandler;
    }

    public static AbstractViewHandlerIntellij getAbstractViewHandler(String projectKey, BasicElement ikasanBasicElement) {
        ViewHandlerCache viewHandlerCache = UiContext.getViewHandlerFactory(projectKey);
        AbstractViewHandlerIntellij viewHandler = null;
        try {
            viewHandler = ((AbstractViewHandlerIntellij)ikasanBasicElement.getOrCreateViewHandler(viewHandlerCache));
        } catch (StudioException se) {
            LOG.warn("STUDIO: A studio exception was raised while trying to get the view handlers, please investigate: " + se.getMessage() + " Trace: " + Arrays.asList(se.getStackTrace()));
        }
        return viewHandler;
    }
}
