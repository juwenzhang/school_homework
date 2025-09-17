package com.blog.monitorservice.collector.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * 简化版的JSON类型映射类
 */
public class JsonBinaryType {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Class<?> returnedClass = Map.class;
}

/**
 * 空接口，仅用于类型标记
 */
interface JsonBinaryTypeMarker {}