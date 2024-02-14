package com.example.service;

import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.dto.CommentUpdateDTO;
import com.example.entity.CommentEntity;
import com.example.exp.AppBadException;
import com.example.repository.CommentRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleService articleService;

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
}
