package org.ikasan.studio.ui.viewmodel;

import org.apache.log4j.Logger;
import org.ikasan.studio.model.ikasan.IkasanFlowComponent;
import org.ikasan.studio.ui.model.IkasanFlowUIComponent;
import org.ikasan.studio.ui.model.IkasanFlowUIComponentFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Abstracts away UI details and provides access to appropriate presentation state from the domain model
 */
public class IkasanFlowExceptionResolverViewHandler extends ViewHandler {
    private static final Logger log = Logger.getLogger(IkasanFlowExceptionResolverViewHandler.class);
    public static final int VERTICAL_PAD = 5;
    public static final int HORIZONTAL_PAD = 5;
    public static final int FLOWCHART_SYMBOL_DEFAULT_HEIGHT = 15;
//    public static final int FLOWCHART_SYMBOL_DEFAULT_HEIGHT = 60;
//    public static final int FLOWCHART_SYMBOL_DEFAULT_WIDTH = 90;
    public static final int FLOWCHART_SYMBOL_DEFAULT_WIDTH = 42;
    int flowchartSymbolHeight = FLOWCHART_SYMBOL_DEFAULT_HEIGHT;
    int flowchartSymbolWidth = FLOWCHART_SYMBOL_DEFAULT_WIDTH;

    IkasanFlowUIComponent ikasanFlowUIComponent;
    IkasanFlowComponent model;

    /**
     * The model can be null e.g. for a pallette item, once dragged onto a canvas, the model would be populated.
     * @param model
     */
    public IkasanFlowExceptionResolverViewHandler(IkasanFlowComponent model) {
        this.model = model;
        if (model != null) {
            ikasanFlowUIComponent = IkasanFlowUIComponentFactory.getInstance().getIkasanFlowUIComponentFromType(model.getType());
        } else {
            ikasanFlowUIComponent = IkasanFlowUIComponentFactory.getInstance().getUNKNOWN();
        }
    }
//
//    public String getPropertiesAsString() {
//        return model.getConfiguredProperties().toString();
//    }

    /**
     * Return the X that the component could reasonable be displayed from the right side of its flow container
     * @param xx
     * @return at least 0, or the X coord that the resolver could be displayed at
     */
    public static int getXOffsetFromRight(int xx) {
        int newX = xx - FLOWCHART_SYMBOL_DEFAULT_WIDTH - HORIZONTAL_PAD;
        if (newX > 0) {
            return newX;
        } else {
            return 0;
        }
    }

    public static int getYOffsetFromTop(int yy) {
        int newY = yy + VERTICAL_PAD;
        if (newY > 0) {
            return newY;
        } else {
            return 0;
        }
    }

    /**
     * Paint the flow icon and the text underneath it
     * @param canvas panel to paint on
     * @param g Swing graphics class
     * @param minimumTopY top y of the component, sometimes we need to supply this, otherwise -1 will allow viewHandler to
     *             determine
     * @return
     */
    public int paintComponent(JPanel canvas, Graphics g, int minimumTopX, int minimumTopY) {
        log.debug("paintComponent invoked");
        // here we ket the components decide x,y
        paintFlowchartSymbol(canvas, g);
        return minimumTopY + flowchartSymbolHeight;
//        return paintSymbolText(g, PaintMode.PAINT);
    }

    private void paintFlowchartSymbol(JPanel canvas, Graphics g) {
        getCanvasIcon().paintIcon(canvas, g, getLeftX(), getTopY());
    }

//    private int paintSymbolText(Graphics g, PaintMode paintMode) {
//        flowchartSymbolHeight = getCanvasIcon().getIconHeight();
//        flowchartSymbolWidth = getCanvasIcon().getIconWidth();
//        int bottomY = StudioUIUtils.drawCenteredStringFromTopCentre(g, paintMode, getText(),
//                getBottomConnectorPoint().x, getBottomConnectorPoint().y + TEXT_VERTICAL_SPACE, flowchartSymbolWidth, null);
//        setHeight(bottomY - getTopY());
//        return bottomY;
//    }

    /**
     * Set the x and y co-ordinates of this component.
     * @param graphics object
     * @param x new x location
     * @param y new y location
     * @param width of container which may be ignored if it is set by the component
     * @param height of container which may be ignored if it is set by the component
     */
    @Override
    public void initialiseDimensions(Graphics graphics, int x, int y, int width, int height) {
        setLeftX(x);
        setTopY(y);
        setWidth(getCanvasIcon().getIconWidth());
        // this has the side effect of setting the correct height.
//        paintSymbolText(graphics, PaintMode.DIMENSION_ONLY);
    }

//    public String getText() {
//        return model.getName();
//    }

//    /**
//     * How close (x,y) does a dragged component need to be to the centre of this component so that we consider it attachable.
//     * @return
//     */
//    public static final Pair<Integer, Integer> getProximityDetect() {
//        return new Pair<>(((FLOWCHART_SYMBOL_DEFAULT_WIDTH) + 5), ((FLOWCHART_SYMBOL_DEFAULT_HEIGHT) + 5));
//    }

    public ImageIcon getCanvasIcon() {
        return ikasanFlowUIComponent.getCanvasIcon();
    }
//
//    @Override
//    public Point getLeftConnectorPoint() {
//        return new Point(getLeftX(), getTopY() + (flowchartSymbolHeight/2));
//    }
//
//    @Override
//    public Point getRightConnectorPoint() {
//        return new Point(getRightX(), getTopY() + (flowchartSymbolHeight/2));
//    }
//
//    @Override
//    public Point getTopConnectorPoint() {
//        return new Point(getLeftX() + (flowchartSymbolWidth/2), getTopY());
//    }
//
//    @Override
//    public Point getBottomConnectorPoint() {
//        return new Point(getLeftX() + (flowchartSymbolWidth/2), getTopY() + flowchartSymbolHeight);
//    }

    public IkasanFlowUIComponent getIkasanFlowUIComponent() {
        return ikasanFlowUIComponent;
    }

    @Override
    public String toString() {
        return "IkasanFlowComponentViewHandler{" +
                "ikasanFlowUIComponent=" + ikasanFlowUIComponent +
                '}';
    }
}
