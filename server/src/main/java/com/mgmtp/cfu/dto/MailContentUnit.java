package com.mgmtp.cfu.dto;

import lombok.*;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MailContentUnit {
    private String id;
    private String style;
    private List<Node> innerContent = new ArrayList<>();
}
