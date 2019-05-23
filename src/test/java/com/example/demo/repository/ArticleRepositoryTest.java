package com.example.demo.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entity.Article;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTest {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void query() {
		Article ipad = new Article();
		ipad.setLibelle("ipad");
		testEntityManager.persistAndFlush(ipad);
		
		Article paddle = new Article();
		paddle.setLibelle("paddle");
		testEntityManager.persistAndFlush(paddle);
		
		Article voiture = new Article();
		voiture.setLibelle("voiture");
		testEntityManager.persistAndFlush(voiture);
		
		List<Article> result = articleRepository.findByQuery("pad");
		Assertions.assertThat(result).hasSize(2);
		Assertions.assertThat(result).extracting(Article:getLibelle).containsExactlyInAnyOrder("ipad", "paddle");
		
	}
}
