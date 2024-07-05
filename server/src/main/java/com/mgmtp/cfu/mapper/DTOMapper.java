package com.mgmtp.cfu.mapper;

public interface DTOMapper <T, U>{
    T toDTO(U entity);
}
