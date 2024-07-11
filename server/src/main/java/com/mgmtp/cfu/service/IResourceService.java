package com.mgmtp.cfu.service;

import org.springframework.core.io.Resource;

public interface IResourceService {
    Resource loadResourceByName(String resourceName);
}
