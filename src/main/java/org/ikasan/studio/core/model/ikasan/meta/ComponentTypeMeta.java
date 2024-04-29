package org.ikasan.studio.core.model.ikasan.meta;


import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.apache.maven.model.Dependency;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class ComponentTypeMeta {
    @lombok.NonNull
    Integer displayOrder;
    @lombok.NonNull
    String componentType;
    @lombok.NonNull
    String componentShortType;
    @JsonSetter(nulls = Nulls.SKIP)         // If the supplied value is null, ignore it.
    private Set<Dependency> jarDependencies;
    @Builder.Default
    private Map<String, ComponentPropertyMeta> properties = new HashMap<>();
}
