package com.mgmtp.cfu.mapper;

public interface EntityMapper<T, U> {
    U toEntity(T dto);
}
