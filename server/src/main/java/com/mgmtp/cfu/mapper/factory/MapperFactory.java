package com.mgmtp.cfu.mapper.factory;

import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.EntityMapper;

import java.util.Optional;

public interface MapperFactory<U> {
    <T> Optional<DTOMapper<T, U>> getDTOMapper(Class<T> dtoClass);
    <T> Optional<EntityMapper<T, U>> getEntityMapper(Class<T> dtoClass);
}
