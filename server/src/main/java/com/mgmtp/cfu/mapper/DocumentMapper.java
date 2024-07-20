package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.documentdto.DocumentDTO;
import com.mgmtp.cfu.entity.Document;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class DocumentMapper implements DTOMapper<DocumentDTO, Document>{
    public abstract DocumentDTO toDTO(Document document);
}
