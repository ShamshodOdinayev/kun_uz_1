package com.example.service;

import com.example.dto.AuthDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.repository.EmailSendHistoryRepository;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import com.example.util.RandomUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private final ProfileRepository profileRepository;
    private final MailSenderService mailSenderService;
    private final EmailSendHistoryRepository emailSendHistoryRepository;
    private final EmailSendHistoryService emailSendHistoryService;
    private final SmsServerService smsServerService;

    public AuthService(ProfileRepository profileRepository, MailSenderService mailSenderService, EmailSendHistoryService emailSendHistoryService, EmailSendHistoryRepository emailSendHistoryRepository, SmsServerService smsServerService) {
        this.profileRepository = profileRepository;
        this.mailSenderService = mailSenderService;
        this.emailSendHistoryService = emailSendHistoryService;
        this.emailSendHistoryRepository = emailSendHistoryRepository;
        this.smsServerService = smsServerService;
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
        dto.setJwt(JWTUtil.encode(entity.getEmail(), entity.getRole()));
        return dto;
    }

    public String registration(RegistrationDTO dto) {
        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();
        if (emailSendHistoryRepository.countSendEmail(dto.getEmail(), from, to) >= 3) {
            throw new AppBadException("To many attempt. Please try after 1 minute.");
        }
        checkEmail(dto);
        isValidPassword(dto.getPassword());
        isValidEmail(dto.getEmail());
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setEmail(dto.getEmail());
        profileRepository.save(entity);

        /**
         * sms yuborish
         **/
        String code = RandomUtil.getRandomSmsCode();
        smsServerService.send(dto.getPhone(), "KunuzTest verification code: ", code);

        /**
         * emailga xabar yuborish
         **/
//        String jwt = JWTUtil.encodeForEmail(entity.getId());
//        String message = getMessage(entity, jwt);
//        emailSendHistoryService.saveHistory(message, dto.getEmail(), entity);
//        mailSenderService.sendEmail(dto.getEmail(), "Registration", message);
        return "A code has been sent to the user's email";
    }

    private static String getMessage(ProfileEntity entity, String jwt) {
        String message = "<h1 style=\"text-align: center\">Hello %s</h1>\n" +
                "<p style=\"background-color: indianred; color: white; padding: 30px\">To complete registration please link to the following link</p>\n" +
                "<a style=\" background-color: #f44336;\n" +
                "  color: white;\n" +
                "  padding: 14px 25px;\n" +
                "  text-align: center;\n" +
                "  text-decoration: none;\n" +
                "  display: inline-block;\" href=\"http://localhost:8080/auth/verification/email/%s\n" +
                "\">Click</a>\n" +
                "<br>\n";
        message = String.format(message, entity.getName(), jwt);
        return message;
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
