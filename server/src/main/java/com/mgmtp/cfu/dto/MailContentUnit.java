package com.mgmtp.cfu.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MailContentUnit {
    private String id;
    private String tag;
    private String content;
    private String href;
}
