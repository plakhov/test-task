package com.home.test.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CustomClassResultMatcher {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> ResultMatcher modelMatches(Class<T> modelType, Consumer<T> matcher) {
        return result -> {
            String content = result.getResponse().getContentAsString(UTF_8);
            T model = OBJECT_MAPPER.readValue(content, modelType);
            matcher.accept(model);
        };
    }

    public static <T> ResultMatcher modelMatches(TypeReference<T> typeReference, Consumer<T> matcher) {
        return result -> {
            String content = result.getResponse().getContentAsString(UTF_8);
            T model = OBJECT_MAPPER.readValue(content, typeReference);
            matcher.accept(model);
        };
    }

}
