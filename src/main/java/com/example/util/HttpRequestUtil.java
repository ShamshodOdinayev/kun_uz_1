package com.example.util;

import com.example.enums.ProfileRole;
import com.example.exp.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;

public class HttpRequestUtil {
    public static Integer getProfileId(HttpServletRequest request, ProfileRole... requiredRole) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        for (ProfileRole profileRole : requiredRole) {
            if (role.equals(profileRole)) {
                return id;
            }
        }
        throw new ForbiddenException("Method not allowed");
    }
}
