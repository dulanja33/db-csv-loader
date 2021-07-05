package com.dulanja33.dcl.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Context {
    private String mappingPath;
    private String csvPath;
    private int chunkSize;
    private YamlMapping yamlMapping;
}
