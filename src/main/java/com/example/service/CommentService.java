package com.example.service;

import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.entity.CommentEntity;
import com.example.exp.AppBadException;
import com.example.repository.CommentRepository;
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

    private static CommentDTO toDTO(CommentEntity entity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(entity.getId());
        commentDTO.setContent(entity.getContent());
        commentDTO.setReplyId(entity.getReplyId());
        commentDTO.setProfileId(entity.getProfileId());
        commentDTO.setCreatedDate(entity.getCreatedDate());
        commentDTO.setArticleId(commentDTO.getArticleId());
        return commentDTO;
    }

    private static CommentEntity CommentCreateDTOtoEntity(CommentCreateDTO dto, Integer profileId) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setProfileId(profileId);
        entity.setReplyId(dto.getReplyId());
        entity.setArticleId(dto.getArticleId());
        entity.setArticleId(dto.getArticleId());
        return entity;
    }
}
