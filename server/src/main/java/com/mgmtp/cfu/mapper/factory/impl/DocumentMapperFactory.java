package com.mgmtp.cfu.mapper.factory.impl;

import com.mgmtp.cfu.entity.Document;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DocumentMapperFactory extends MapperFactoryImpl<Document> {

    public DocumentMapperFactory(Set<DTOMapper<?, Document>> dtoMappers,
                                 Set<EntityMapper<?, Document>> entityMappers) {
        _dtoMappers = dtoMappers.stream()
                .collect(Collectors.toMap(MapperFactoryImpl::extractDTOType, Function.identity()));

        _entityMappers = entityMappers.stream()
                .collect(Collectors.toMap(MapperFactoryImpl::extractDTOType, Function.identity()));
    }
}
