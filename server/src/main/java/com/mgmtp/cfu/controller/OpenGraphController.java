package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.service.OpenGraphService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/opengraph")
public class OpenGraphController {
    @Autowired
    private OpenGraphService openGraphService;

    @GetMapping()
    public Map<String, String> getCourseInfo(@RequestParam String url) {
        return openGraphService.getCourseInfo(url);
    }
}
