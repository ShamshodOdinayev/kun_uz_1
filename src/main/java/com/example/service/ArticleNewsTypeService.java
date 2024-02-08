package com.example.service;

import com.example.entity.ArticleNewsTypeEntity;
import com.example.repository.ArticleNewsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
//        //[1,5] - old
        //  [7,5,5]- typesIdList
//        //[7,5] - new
        List<ArticleNewsTypeEntity> optionalArticleNewsType = articleNewsTypeRepository.findByArticleIdAndVisible(articleId, true);
        List<Integer> old = new LinkedList<>();
        List<Integer> newAry = new LinkedList<>();

        for (ArticleNewsTypeEntity entity : optionalArticleNewsType) {
            old.add(entity.getTypesId());
        }
        if (old.isEmpty()) {
            create(articleId, typesIdList);
        }
        for (int i = 0; i < old.size(); i++) {
            for (int j = 0; j < typesIdList.size(); j++) {
                if (old.get(i) == typesIdList.get(j)) {
                    newAry.add(old.get(i));
                }
            }
        }
        for (int i = 0; i < typesIdList.size(); i++) {
            for (int j = 0; j < newAry.size(); j++) {
                if (typesIdList.get(i) != newAry.get(j)) {
                    newAry.add(typesIdList.get(i));
                }
            }
        }
        for (Integer i : old) {
            articleNewsTypeRepository.updateByArticleIdAndTypesId(articleId, i);
        }

        for (Integer i : newAry) {
            create(articleId, i);
        }







       /* for (int i = 0; i < typesIdList.size(); i++) {
            for (ArticleNewsTypeEntity entity : optionalArticleNewsType) {
                if (typesIdList.get(i) != entity.getTypesId()) {
                    articleNewsTypeRepository.deleteByTypesId(entity.getTypesId(), articleId);
                }
            }
        }
        create(articleId, typesIdList);*/

    }

    public List<ArticleNewsTypeEntity> getLastArticleByType(Long typeId, Integer size) {
        return articleNewsTypeRepository.findByTypesIdAndVisibleOrderByCreatedDate(typeId, true);
    }
}
