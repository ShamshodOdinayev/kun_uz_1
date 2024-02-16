package com.example.service;

import com.example.entity.CommentLikeEntity;
import com.example.enums.LikeStatus;
import com.example.exp.AppBadException;
import com.example.repository.CommentLikeRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public Boolean like(Integer commentId) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        Optional<CommentLikeEntity> commentLikeEntityOptional = commentLikeRepository.findByCommentIdAndProfileId(commentId, profileId);
        if (commentLikeEntityOptional.isPresent()) {
            return true;
        }
        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setStatus(LikeStatus.LIKE);
        entity.setCommentId(commentId);
        entity.setProfileId(profileId);
        commentLikeRepository.save(entity);
        return true;
    }

    public Boolean dislike(Integer commentId) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        Optional<CommentLikeEntity> commentLikeEntityOptional = commentLikeRepository.findByCommentIdAndProfileId(commentId, profileId);
        if (commentLikeEntityOptional.isEmpty()) {
            throw new AppBadException("like not found");
        }
        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setStatus(LikeStatus.DISLIKE);
        commentLikeRepository.save(entity);
        return true;
    }

    public Boolean remove(Integer commentId) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        Optional<CommentLikeEntity> commentLikeEntityOptional = commentLikeRepository.findByCommentIdAndProfileId(commentId, profileId);
        if (commentLikeEntityOptional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        if (commentLikeEntityOptional.get().getProfileId().equals(profileId)) {
            commentLikeRepository.remove(commentLikeEntityOptional.get().getId());
            return true;
        }
        return false;
    }
}
