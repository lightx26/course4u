package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.entity.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class RegistrationOverviewMapper implements DTOMapper<RegistrationOverviewDTO, Registration>{
    @Override
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "courseThumbnailUrl", source = "course.thumbnailUrl")
    @Mapping(target = "coursePlatform", source = "course.platform")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userFullname", source = "user.fullName")
    @Mapping(target = "userAvatarUrl", source = "user.avatarUrl")
    public abstract RegistrationOverviewDTO toDTO(Registration registration);
}
