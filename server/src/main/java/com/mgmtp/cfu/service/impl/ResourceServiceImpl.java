package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.service.IResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements IResourceService {
    private final ResourceLoader resourceLoader;
    @Override
    public Resource loadResourceByName(String resourceName) {
        return resourceLoader.getResource("classpath:" + resourceName);
    }
}
