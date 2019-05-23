package com.example.demo.entity;

import java.util.HashSet;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FactureTest {

	@Test
	public void getTotal_factureVideSansArticleRetourneZero() {
		Facture facture = new Facture();
		facture.setLigneFactures(new HashSet<>());
		
		Double total = facture.getTotal();
		
		Assertions.assertThat(total).isEqualTo(0);
	}
	
	@Test
	public void getTotalfactureAvecUnArticleRetournePrix() {
		Facture facture = new Facture();
		Article article = new Article();
		LigneFacture ligneFacture = new LigneFacture();
		HashSet<LigneFacture>ligneFactures = new HashSet<>();
		
		
		article.setId((long) 1);
		article.setPrix(250);
		
		ligneFacture.setArticle(article);
		ligneFacture.setQuantite(1);
		ligneFactures.add(ligneFacture);
		
		facture.setLigneFactures(ligneFactures);
		
		Double total = facture.getTotal();
		
		Assertions.assertThat(total).isEqualTo(250);
	}
	
	@Test
	public void getTotalfactureAvecUnArticleRetournePrixQuantiteDeux() {
		Facture facture = new Facture();
		Article article = new Article();
		LigneFacture ligneFacture = new LigneFacture();
		HashSet<LigneFacture>ligneFactures = new HashSet<>();
		
		
		article.setId((long) 1);
		article.setPrix(250);
		
		ligneFacture.setArticle(article);
		ligneFacture.setQuantite(2);
		ligneFactures.add(ligneFacture);
		
		facture.setLigneFactures(ligneFactures);
		
		Double total = facture.getTotal();
		
		Assertions.assertThat(total).isEqualTo(500);
	}
}
