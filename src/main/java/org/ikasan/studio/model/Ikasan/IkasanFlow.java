package org.ikasan.studio.model.Ikasan;

import org.ikasan.studio.SUtils;
import org.ikasan.studio.ui.viewmodel.IkasanFlowViewHandler;
import org.ikasan.studio.ui.viewmodel.ViewHandler;

import java.util.ArrayList;
import java.util.List;

public class IkasanFlow {
    private ViewHandler viewHandler;
    private String name;
    private String javaVariableName;
    private String description;
    private IkasanFlowElement input;
    private IkasanFlowElement output;

    private List<IkasanFlowElement> flowElementList = new ArrayList<>();

    public IkasanFlow() {
        viewHandler = new IkasanFlowViewHandler(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
        if (name != null && name.length() > 0) {
            javaVariableName = SUtils.toJavaIdentifier(name);
        }
    }

    public String getJavaVariableName() {
        return javaVariableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IkasanFlowElement> getFlowElementList() {
        return flowElementList;
    }

    public boolean addFlowElement(IkasanFlowElement ikasanFlowElement) {
        return flowElementList.add(ikasanFlowElement);
    }

    public ViewHandler getViewHandler() {
        return viewHandler;
    }

    public IkasanFlowElement getInput() {
        return input;
    }

    public void setInput(IkasanFlowElement input) {
        this.input = input;
    }

    public IkasanFlowElement getOutput() {
        return output;
    }

    public void setOutput(IkasanFlowElement output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "IkasanFlow{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", flowElementList=" + flowElementList +
                '}';
    }


}
