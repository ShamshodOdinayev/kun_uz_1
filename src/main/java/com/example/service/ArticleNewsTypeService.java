package com.example.service;

import com.example.entity.ArticleNewsTypeEntity;
import com.example.repository.ArticleNewsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArticleNewsTypeService {
    @Autowired
    private ArticleNewsTypeRepository articleNewsTypeRepository;

    public void create(String articleId, List<Integer> typesIdList) {
        for (Integer typeId : typesIdList) {
            create(articleId, typeId);
        }
    }

    public void create(String articleId, Integer typesId) {
        ArticleNewsTypeEntity entity = new ArticleNewsTypeEntity();
        entity.setArticleId(articleId);
        entity.setTypesId(typesId);
        articleNewsTypeRepository.save(entity);
    }

    public void merge(String articleId, List<Integer> typesIdList) {
//        // create
//        // [] old
//        // [1,2,3,4,5] new
//
//        // update
//        //[1,2,3,4,5] - old
//        //[7,5] - new
        List<ArticleNewsTypeEntity> optionalArticleNewsType = articleNewsTypeRepository.findByArticleId(articleId);
        for (int i = 0; i < typesIdList.size(); i++) {
            for (ArticleNewsTypeEntity entity : optionalArticleNewsType) {
                if (typesIdList.get(i) != entity.getTypesId()) {
                    articleNewsTypeRepository.deleteByTypesId(entity.getTypesId(), articleId);
                }
            }
        }
        create(articleId, typesIdList);

    }

}
