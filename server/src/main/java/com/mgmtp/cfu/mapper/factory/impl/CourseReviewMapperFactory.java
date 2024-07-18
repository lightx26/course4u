package com.mgmtp.cfu.mapper.factory.impl;

import com.mgmtp.cfu.entity.CourseReview;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CourseReviewMapperFactory extends MapperFactoryImpl<CourseReview> {
    @Autowired
    public CourseReviewMapperFactory(Set<DTOMapper<?, CourseReview>> dtoMappers,
                                      Set<EntityMapper<?, CourseReview>> entityMappers) {
        _dtoMappers = dtoMappers.stream()
                .collect(Collectors.toMap(MapperFactoryImpl::extractDTOType, Function.identity()));

        _entityMappers = entityMappers.stream()
                .collect(Collectors.toMap(MapperFactoryImpl::extractDTOType, Function.identity()));
    }
}
