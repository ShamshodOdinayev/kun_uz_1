package com.example.service;

import com.example.dto.AuthDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.entity.EmailSendHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private final ProfileRepository profileRepository;
    private final MailSenderService mailSenderService;
    private final EmailSendHistoryService emailSendHistoryService;

    public AuthService(ProfileRepository profileRepository, MailSenderService mailSenderService, EmailSendHistoryService emailSendHistoryService) {
        this.profileRepository = profileRepository;
        this.mailSenderService = mailSenderService;
        this.emailSendHistoryService = emailSendHistoryService;
    }

    public ProfileDTO auth(AuthDTO profile) { // login
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPassword(profile.getEmail(),
                MD5Util.encode(profile.getPassword()));
        if (optional.isEmpty()) {
            throw new AppBadException("Email or Password is wrong");
        }
        if (!optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException("Profile not active");
        }
        ProfileEntity entity = optional.get();
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setRole(entity.getRole());
        dto.setJwt(JWTUtil.encode(entity.getId(), entity.getRole()));
        return dto;
    }

    public String registration(RegistrationDTO dto) {
        checkEmail(dto);
        isValidPassword(dto.getPassword());
        isValidEmail(dto.getEmail());
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setEmail(dto.getEmail());
        profileRepository.save(entity);
        String jwt = JWTUtil.encodeForEmail(entity.getId());
        String message = "Hello. \n To complete registration please link to the following link\n"
                + "http://localhost:8080/auth/verification/email/" + jwt;
        emailSendHistoryService.saveHistory(message, dto.getEmail());
        mailSenderService.sendEmail(dto.getEmail(), "Registration", message);
        return "A code has been sent to the user's email";
    }

    public String emailVerification(String jwt) {
        try {
            JwtDTO jwtDTO = JWTUtil.decode(jwt);
            Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
            if (optional.isEmpty()) {
                throw new AppBadException("Profile not found");
            }
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
                throw new AppBadException("Profile in wrong status");
            }
            profileRepository.updateStatus(entity.getId(), ProfileStatus.ACTIVE);
        } catch (JwtException e) {
            throw new AppBadException("Please tyre again.");
        }
        return "Successful";
    }


    /**
     * Regex shartlari:
     * ^[a-zA-Z0-9_+&*-]+: Foydalanuvchi nomi
     * (?:\\.[a-zA-Z0-9_+&*-]+)*: Qanday qilib nuqta orqali yana qo'shish mumkin
     *
     * @: @ belgisi
     * (?:[a-zA-Z0-9-]+\\.)+: Domen nomi
     * [a-zA-Z]{2,7}$: Uchun top-domains (masalan, .com, .net, .org)
     **/

    public static void isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new AppBadException("Not a valid email");
        }
    }

    private void checkEmail(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.deleteByEmail(dto.getEmail());
            } else {
                throw new AppBadException("The email cannot be the same");
            }
        }
    }

    public static void isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new AppBadException("Not a valid password (Must contain an uppercase letter, a lowercase letter, and a number, length 8)");
        }
    }
}
