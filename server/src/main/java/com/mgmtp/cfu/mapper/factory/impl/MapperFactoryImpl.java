package com.mgmtp.cfu.mapper.factory.impl;

import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.EntityMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

class MapperFactoryImpl<U> implements MapperFactory<U> {

    protected Map<Class<?>, DTOMapper<?, U>> _dtoMappers;
    protected Map<Class<?>, EntityMapper<?, U>> _entityMappers;

    protected static Class<?> extractDTOType(Object mapper) {
        Type[] genericInterfaces = mapper.getClass().getGenericInterfaces();
        for (Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType().equals(DTOMapper.class) ||
                        parameterizedType.getRawType().equals(EntityMapper.class)) {
                    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
            }
        }
        throw new IllegalArgumentException("Invalid mapper type: " + mapper.getClass());
    }

    @Override
    public <T> Optional<DTOMapper<T, U>> getDTOMapper(Class<T> dtoClass) {
        return Optional.ofNullable((DTOMapper<T, U>) _dtoMappers.get(dtoClass));
    }

    @Override
    public <T> Optional<EntityMapper<T, U>> getEntityMapper(Class<T> dtoClass) {
        return Optional.ofNullable((EntityMapper<T, U>) _entityMappers.get(dtoClass));
    }
}
