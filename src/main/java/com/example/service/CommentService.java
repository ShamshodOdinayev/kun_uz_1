package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.*;
import com.example.entity.CommentEntity;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadException;
import com.example.repository.CommentCustomRepository;
import com.example.repository.CommentRepository;
import com.example.util.SpringSecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentCustomRepository commentCustomRepository;

    public CommentDTO create(CommentCreateDTO dto, Integer profileId) {
        articleService.get(dto.getArticleId());
        if (dto.getReplyId() != null) {
            Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(dto.getReplyId());
            if (optionalCommentEntity.isEmpty()) {
                throw new AppBadException("Reply comment not found");
            }
        }
        CommentEntity entity = CommentCreateDTOtoEntity(dto, profileId);
        commentRepository.save(entity);
        return toDTO(entity);
    }

    private CommentDTO toDTO(CommentEntity entity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(entity.getId());
        commentDTO.setContent(entity.getContent());
        commentDTO.setReplyId(entity.getReplyId());
        commentDTO.setProfileId(entity.getProfileId());
        commentDTO.setCreatedDate(entity.getCreatedDate());
        commentDTO.setArticleId(commentDTO.getArticleId());
        return commentDTO;
    }

    private CommentEntity CommentCreateDTOtoEntity(CommentCreateDTO dto, Integer profileId) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setProfileId(profileId);
        entity.setReplyId(dto.getReplyId());
        entity.setArticleId(dto.getArticleId());
        entity.setArticleId(dto.getArticleId());
        return entity;
    }

    public CommentDTO update(CommentUpdateDTO dto, Integer id) {
        Optional<CommentEntity> commentEntityOptional = commentRepository.findByIdAndVisible(id, true);
        if (commentEntityOptional.isEmpty()) {
            throw new AppBadException("Comment not found");
        }
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        CommentEntity entity = commentEntityOptional.get();
        if (!entity.getProfileId().equals(profileId)) {
            throw new AppBadException("Has no such right");
        }
        entity.setContent(dto.getContent() != null ? dto.getContent() : entity.getContent());
        entity.setReplyId(dto.getReplyId() != null ? dto.getReplyId() : entity.getReplyId());
        entity.setArticleId(dto.getArticleId() != null ? dto.getArticleId() : entity.getArticleId());
        commentRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean delete(Integer id) {
        Optional<CommentEntity> commentEntityOptional = commentRepository.findByIdAndVisible(id, true);
        if (commentEntityOptional.isEmpty()) {
            throw new AppBadException("Comment not found");
        }
        CustomUserDetails currentUser = SpringSecurityUtil.getCurrentUser();
        if (commentEntityOptional.get().getProfileId().equals(currentUser.getId())) {
            commentRepository.deleteById(id);
            return true;
        } else if (currentUser.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            commentRepository.deleteById(id);
            return true;
        }
        throw new AppBadException("Not allowed");
    }

    public List<CommentDTO> getByArticleId(String articleId) {
        List<CommentEntity> entityList = commentRepository.findByArticleIdAndVisible(articleId, true);
        List<CommentDTO> dtoList = new LinkedList<>();
        for (CommentEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    private CommentDTO entityToDTO(CommentEntity entity) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(entity.getProfileId());
        profileDTO.setName(entity.getProfile().getName());
        profileDTO.setSurname(entity.getProfile().getSurname());
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setContent(entity.getContent());
        dto.setProfile(profileDTO);
        return dto;
    }

    public PageImpl<CommentDTO> filter(CommentFilterDTO dto, Integer page, Integer size) {
        PaginationResultDTO<CommentEntity> paginationResultDTO = commentCustomRepository.filter(dto, page, size);
        List<CommentEntity> entityList = paginationResultDTO.getList();
        List<CommentDTO> dtoList = new LinkedList<>();
        for (CommentEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(dtoList, pageable, paginationResultDTO.getTotalSize());
    }

    public PageImpl<CommentDTO> pagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CommentEntity> courseEntityPage = commentRepository.findAll(pageable);
        List<CommentEntity> courseEntityList = courseEntityPage.getContent();
        long totalElements = courseEntityPage.getTotalElements();
        List<CommentDTO> dtoList = new LinkedList<>();
        for (CommentEntity courseEntity : courseEntityList) {
            dtoList.add(entityToDTO(courseEntity));
        }
        return new PageImpl<>(dtoList, pageable, totalElements);
    }
}
