package com.mgmtp.cfu.dto.documentdto;

import com.mgmtp.cfu.enums.DocumentStatus;
import com.mgmtp.cfu.enums.DocumentType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentDTO {
    private Long id;

    private Long registrationId;

    private String url;

    private DocumentStatus status;

    private DocumentType type;
}
