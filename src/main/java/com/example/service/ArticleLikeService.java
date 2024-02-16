package com.example.service;

import com.example.entity.ArticleLikeEntity;
import com.example.enums.LikeStatus;
import com.example.exp.AppBadException;
import com.example.repository.ArticleLikeRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;

    public ArticleLikeService(ArticleLikeRepository articleLikeRepository) {
        this.articleLikeRepository = articleLikeRepository;
    }

    public Boolean like(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        Optional<ArticleLikeEntity> byArticleIdAndProfileId = articleLikeRepository.findByArticleIdAndProfileId(articleId, profileId);
        if (byArticleIdAndProfileId.isPresent()) {
            return true;
        }
        ArticleLikeEntity entity = new ArticleLikeEntity();
        entity.setArticleId(articleId);
        entity.setStatus(LikeStatus.LIKE);
        entity.setProfileId(profileId);
        articleLikeRepository.save(entity);
        return true;
    }

    public Boolean dislike(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        Optional<ArticleLikeEntity> byArticleIdAndProfileId = articleLikeRepository.findByArticleIdAndProfileId(articleId, profileId);
        if (byArticleIdAndProfileId.isEmpty()) {
            throw new AppBadException("like not found");
        }
        ArticleLikeEntity entity = byArticleIdAndProfileId.get();
        entity.setStatus(LikeStatus.DISLIKE);
        articleLikeRepository.save(entity);
        return true;
    }

    public Boolean remove(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        Optional<ArticleLikeEntity> allByArticleIdAndProfileId = articleLikeRepository.findByArticleIdAndProfileId(articleId, profileId);
        if (allByArticleIdAndProfileId.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        if (allByArticleIdAndProfileId.get().getProfileId().equals(profileId)) {
            articleLikeRepository.deleteById(allByArticleIdAndProfileId.get().getId());
            return true;
        }
        return false;
    }
}
