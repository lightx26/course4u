package com.mgmtp.cfu.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryCourseRequestDTOConverter implements Converter<String, List<CourseRequest.CategoryCourseRequestDTO>> {

    @SneakyThrows
    @Override
    public List<CourseRequest.CategoryCourseRequestDTO> convert(@NotNull String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        //support for swagger-ui as it doesn't translate the correct format for array list
        if (!source.startsWith("[") && !source.endsWith("]")) {
            source = "[" + source + "]";
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(source, new TypeReference<>() {});
    }
}
