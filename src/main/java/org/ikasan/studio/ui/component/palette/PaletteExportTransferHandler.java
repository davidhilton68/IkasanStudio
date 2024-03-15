package org.ikasan.studio.ui.component.palette;

import com.intellij.openapi.diagnostic.Logger;
import org.ikasan.studio.build.model.ikasan.instance.FlowElement;
import org.ikasan.studio.build.model.ikasan.instance.FlowElementFactory;
import org.ikasan.studio.ui.model.IkasanFlowUIComponentTransferable;
import org.ikasan.studio.ui.model.PaletteItem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class PaletteExportTransferHandler extends TransferHandler // implements Transferable
{
    private static final Logger LOG = Logger.getInstance("#PaletteExportTransferHandler");
    private static final DataFlavor ikasanFlowUIComponentFlavor = new DataFlavor(FlowElement.class, "FlowElement");
    private static final DataFlavor[] flavors = { ikasanFlowUIComponentFlavor };

    // Source actions i.e. methods called for the source of the copy

    /**
     * For PaletteExportTransferHandler we only want to copy a component onto the design window, not link or move
     * @param sourceComponent the sourceComponent
     * @return
     */
    @Override
    public int getSourceActions(JComponent sourceComponent) {
        return TransferHandler.COPY;
    }

    @Override
    public Transferable createTransferable(JComponent sourceComponent) {
        if (sourceComponent instanceof JList paletteList &&
                ((JList)sourceComponent).getSelectedValue() instanceof PaletteItem &&
                ! ((PaletteItem)((JList)sourceComponent).getSelectedValue()).isCategory()) {
            PaletteItem item = (PaletteItem)paletteList.getSelectedValue();
            IkasanFlowUIComponentTransferable newTransferable = new IkasanFlowUIComponentTransferable(
                    FlowElementFactory.createFlowElement(item.getComponentMeta(), null));
            Image dragImage = item.getComponentMeta().getSmallIcon().getImage();
            if (dragImage != null) {
                setDragImage(dragImage);
            }

            return newTransferable;
        }

        return null;
    }
    //These methods are invoked for the drop gesture, or the paste action, when the component is the target of the operation

    /**
     * This method is called repeatedly during a drag gesture and returns true if the area below the cursor can accept the
     * transfer, or false if the transfer will be rejected.
     * For example, a flow can be dropped anywhere on the canvas, so it will be true for the whole canvas but
     * a component can only be dropped inside a flow, so it will be false until the mouse is over a flow.
     * @param targetComponent that the mouse is over that has registered this as its Transfer Handler.
     * @param destinationSupportedflavors
     * @return false all the time currently
     */
    @Override
    public boolean canImport(JComponent targetComponent, DataFlavor[] destinationSupportedflavors) {
        // This is the TransferHandler used while hovvering over the Palette itself, we don't want to (currently) import - always false.
        return false;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        LOG.info("Got " + support);
        return super.importData(support);

    }

    /**
     * This method is called on a successful drop (or paste) and initiates the transfer of data to the target component.
     * This method returns true if the import was successful and false otherwise.
     * @param targetComponent under the mouse that has registered as being able to receive this flavor of component.
     * @param t the data object being dragged.
     * @return true if the import was a success
     */
    @Override
    public boolean importData(JComponent targetComponent, Transferable t) {

        if (targetComponent instanceof JPanel) {
            if (t.isDataFlavorSupported(ikasanFlowUIComponentFlavor)) {
                try {
                    IkasanFlowUIComponentTransferable ikasanFlowUIComponent = (IkasanFlowUIComponentTransferable) t.getTransferData(ikasanFlowUIComponentFlavor);
                    LOG.warn("I can perhaps drop " + ikasanFlowUIComponent);

                    return true;
                } catch (UnsupportedFlavorException | IOException ignored) {
                }
            }
        }
        return false;
    }

}

