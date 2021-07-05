package com.dulanja33.dcl.properties;


import lombok.Data;

import java.util.List;

@Data
public class YamlMapping {
    private List<MapData> mapping;
    private String checkQuery;
    private String insertQuery;

    @Data
    public static class MapData {
        private String csvCol;
        private String dbCol;
        private String transformer;
    }
}


