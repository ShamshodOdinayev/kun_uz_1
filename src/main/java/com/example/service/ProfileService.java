package com.example.service;

import com.example.dto.CreateProfileDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDTO create(CreateProfileDTO dto) {
        isValidPassword(dto.getPassword());
        if (dto.getEmail() == null || dto.getName() == null || dto.getSurName() == null || dto.getRole() == null || dto.getStatus() == null) {
            throw new AppBadException("Some parameter is null. All fields must be given a value!!!");
        }
        checkEmail(dto);
        ProfileEntity entity = toEntity(dto);
        profileRepository.save(entity);
        return toDTO(entity);
    }

    private void checkEmail(CreateProfileDTO dto) {
        Optional<ProfileEntity> email = profileRepository.findByEmail(dto.getEmail());
        if (email.isPresent()) {
            throw new AppBadException("The email cannot be the same");
        }
    }

    private static ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(entity.getId());
        profileDTO.setName(entity.getName());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setEmail(entity.getEmail());
        profileDTO.setRole(entity.getRole());
        profileDTO.setStatus(entity.getStatus());
        profileDTO.setCreatedDate(entity.getCreatedDate());
        profileDTO.setVisible(entity.getVisible());
        profileDTO.setJwt(JWTUtil.encode(entity.getId(), entity.getRole()));
        return profileDTO;
    }

    private static ProfileEntity toEntity(CreateProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurName());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static void isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new AppBadException("Not a valid password (Must contain an uppercase letter, a lowercase letter, and a number, length 8)");
        }
    }

    public ProfileDTO update(CreateProfileDTO dto, Integer id) {
        ProfileEntity entity = get(id);
        return entityUpdate(dto, entity);
    }

    public ProfileDTO update(CreateProfileDTO dto, String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        ProfileEntity entity = get(jwtDTO.getId());
        return entityUpdate(dto, entity);
    }

    private ProfileDTO entityUpdate(CreateProfileDTO dto, ProfileEntity entity) {
        entity.setName(dto.getName() == null ? entity.getName() : dto.getName());
        entity.setSurname(dto.getSurName() == null ? entity.getSurname() : dto.getSurName());
        entity.setEmail(dto.getEmail() == null ? entity.getEmail() : dto.getEmail());
        entity.setRole(dto.getRole() == null ? entity.getRole() : dto.getRole());
        entity.setPassword(dto.getPassword() == null ? entity.getPassword() : dto.getPassword());
        entity.setStatus(dto.getStatus() == null ? entity.getStatus() : dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return toDTO(entity);
    }

    public List<ProfileDTO> getAll() {
        List<ProfileDTO> dtoList = new LinkedList<>();
        Iterable<ProfileEntity> entityIterable = profileRepository.findAll();
        for (ProfileEntity entity : entityIterable) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    public Boolean deleteById(Integer id) {
        Optional<ProfileEntity> entityOptional = profileRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new AppBadException("No user found with this id");
        }
        profileRepository.deleteByIdQuery(id);
        return true;
    }

    private ProfileEntity get(Integer id) {
        Optional<ProfileEntity> entityOptional = profileRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new AppBadException("Profile not found");
        }
        return entityOptional.get();
    }
}
