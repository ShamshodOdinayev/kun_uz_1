package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDTO {
    private Integer id;
    private ProfileRole role;
    private String email;

    public JwtDTO(ProfileRole role, String email) {
        this.role = role;
        this.email = email;
    }

    public JwtDTO(Integer id, ProfileRole role) {
        this.id = id;
        this.role = role;
    }

    public JwtDTO(Integer id) {
        this.id = id;
    }
}
