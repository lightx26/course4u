package com.mgmtp.cfu.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse {
    private int totalElements;
    private Object list;

}
