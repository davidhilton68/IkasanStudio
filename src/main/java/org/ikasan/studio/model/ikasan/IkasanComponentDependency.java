package org.ikasan.studio.model.ikasan;

import org.apache.maven.model.Dependency;
import org.ikasan.studio.StudioUtils;

import java.util.Map;

public enum IkasanComponentDependency {
    BASIC(),
    JMS(),
    NONE();

    Map<String, Dependency> dependencies;

    IkasanComponentDependency() {
        dependencies = StudioUtils.readIkasanComponentDependencies(this.toString());
    }

    public Map<String, Dependency> getDependencies() {
        return dependencies;
    }
}
