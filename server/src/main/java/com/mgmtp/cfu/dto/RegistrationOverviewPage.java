package com.mgmtp.cfu.dto;

import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.mapper.RegistrationMapper;
import com.mgmtp.cfu.util.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationOverviewPage {
    private final int pageTotal;
    private final int page;
    private final int limit;
    private final List<Registration> registrations;

    public RegistrationOverviewPage(final int page, List<Registration> registrations) {
        this.limit = Constant.MY_REGISTRATION_PAGE_LIMIT;
        this.page = (page <= 0) ? 1 : page;
        this.registrations = (registrations == null) ? new ArrayList<>() : registrations;

        int totalRegistrations = this.registrations.size();
        this.pageTotal = (int) Math.ceil((double) totalRegistrations / limit);
    }

    public List<MyRegistrationOverviewDTO> getDtoPage( ) {
        if (pageTotal == 0 || page > pageTotal) {
            return Collections.emptyList();
        }

        int startIndex = (page - 1) * limit;
        int endIndex = Math.min(startIndex + limit, registrations.size());

        return registrations.subList(startIndex, endIndex).stream()
                .map(RegistrationMapper::toMyRegistrationDto)
                .collect(Collectors.toList());
    }
}
