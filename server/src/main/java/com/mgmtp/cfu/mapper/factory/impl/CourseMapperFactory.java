package com.mgmtp.cfu.mapper.factory.impl;

import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Qualifier("CourseMapper")
public class CourseMapperFactory extends MapperFactoryImpl<Course> {
    @Autowired
    public CourseMapperFactory(Set<DTOMapper<?, Course>> dtoMappers,
                               Set<EntityMapper<?, Course>> entityMappers) {
        _dtoMappers = dtoMappers.stream()
                .collect(Collectors.toMap(MapperFactoryImpl::extractDTOType, Function.identity()));

        _entityMappers = entityMappers.stream()
                .collect(Collectors.toMap(MapperFactoryImpl::extractDTOType, Function.identity()));
    }
}
