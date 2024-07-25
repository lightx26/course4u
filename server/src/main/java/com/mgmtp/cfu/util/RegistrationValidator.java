package com.mgmtp.cfu.util;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.exception.RegistrationFieldNotFoundException;
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException;

import java.lang.reflect.Field;
import java.util.Objects;

public class RegistrationValidator {
    public static boolean isDefaultStatus(String status) {
        if(Objects.isNull(status) || status.trim().isEmpty())
            return true;
        return status.toUpperCase().equals("ALL") || status.toLowerCase().equals("default");
    }

    /**
     * Function to validate parameters, immediately throw an Exception and response when invalid
     * @param registrationOverviewParams: parameters
     */

    public static void validateRegistrationOverviewParams(RegistrationOverviewParams registrationOverviewParams){
        String status = registrationOverviewParams.getStatus();
        String orderBy = registrationOverviewParams.getOrderBy();

        try{
            if (!status.isEmpty() && !status.equalsIgnoreCase("all")){
                RegistrationStatus.valueOf(status.toUpperCase());
            }
        }
        catch (IllegalArgumentException err){
            throw new RegistrationStatusNotFoundException("Error while reading status: " + err.getMessage());
        }


        if (!orderBy.isEmpty()){
            boolean foundField = Boolean.FALSE;
            for (Field field : Registration.class.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(orderBy.toLowerCase())) {
                    foundField = Boolean.TRUE;
                    break;
                }
            }

            if (!foundField) {
                throw new RegistrationFieldNotFoundException("Error while reading orderBy: field not found " + orderBy);
            }
        }

    }
}
