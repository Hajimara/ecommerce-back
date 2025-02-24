package com.shop.constant.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilString {

    private UtilString() {
        // 이 클래스는 정적(static) 메서드만 제공합니다.
        // change, to 등 인자 값을 받아 형태를 변형하여 결과를 제공하는 함수입니다.
    }

    public static <T> T changeJsonToClass(
            String filePath,
            Class<T> typeClass
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper = objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readValue(new File(filePath), typeClass);
    }

    public static Map<String, Object> changeStringToMap(String contents) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(contents, new TypeReference<>() {});
    }

    public static String toCamelCaseFirstUpper(String data) {
        String[] partList = data.split("-");

        return Arrays.stream(partList)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining());
    }

    public static String toFirstUpper(String data) {
        return StringUtils.capitalize(data);
    }

    public static String toFirstLower(String data) {
        return StringUtils.uncapitalize(data);
    }

    public static List<String> toList(Object data) {
        if (data != null) {
            String[] dataList = data.toString().split(",");

            return Arrays.asList(dataList);
        } else {
            return Collections.emptyList();
        }
    }

    public static String escapeLikePattern(String keyword) {
        return "%" +
                keyword.replace("=", "\\=")
                        .replace("+", "\\+")
                        .replace("%", "\\%")
                        .replace("[", "\\[")
                        .replace("]", "\\]")
                        .replace("^", "\\^")
                        .replace("_", "\\_") +
                "%";
    }
}
