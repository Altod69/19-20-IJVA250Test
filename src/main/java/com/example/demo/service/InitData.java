package com.example.demo.service;

import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

/**
 * Classe permettant d'insérer des données dans l'application.
 */
@Service
@Transactional
public class InitData implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EntityManager em;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        insertTestData();
    }

    private void insertTestData() {
        Client client1 = newClient("PETRILLO", "Alexandre", LocalDate.of(1983, 1, 3));
        em.persist(client1);

        Client client2 = newClient("Dupont", "Jérome", LocalDate.of(1990, 1, 22));
        em.persist(client2);

        Client client3 = newClient("D\"oe", "Jo;hn", LocalDate.of(2000, 1, 3));
        em.persist(client3);

        Client client4 = newClient("Theodore", "Alexandre", LocalDate.of(1983, 6, 14));
        em.persist(client4);
        
        Article a1 = new Article();
        a1.setLibelle("Balayette");
        a1.setDescriptif("Balayette en plastique rose, qualité douteuse");
        a1.setPrix(3.99);
        em.persist(a1);

        Article a2 = new Article();
        a2.setLibelle("Stylo espion");
        a2.setDescriptif("Stylo bic ordinaire de couleur bleue, ayant potentiellement appartenu à Sean Connery");
        a2.setPrix(130);
        em.persist(a2);

        Article a3 = new Article();
        a3.setLibelle("Ipad PRO");
        a3.setDescriptif("Tablette numérique fabriqué par la secte technologique APPLE, vendue généralement deux fois le prix de sa valeur réelle");
        a3.setPrix(799);
        em.persist(a3);
        
        Article a4 = new Article();
        a4.setLibelle("Costume Batman");
        a4.setDescriptif("Costume en élastomère avec prothèse de biceps");
        a4.setPrix(799);
        em.persist(a4);
        
        Article a5 = new Article();
        a5.setLibelle("Pistolaser");
        a5.setDescriptif("Arme à feu légendaire ayant appartenu à Han Solo. Modèle gravé");
        a5.setPrix(3850);
        em.persist(a5);
        
        Article a6 = new Article();
        a6.setLibelle("Faucon Millenium");
        a6.setDescriptif("Le seul, l'unique. 3eme main, kilométrage: 42M années lumières, hyperdrive neuf. livré avec deux droïdes");
        a6.setPrix(357000);
        em.persist(a6);
        
        Article a7 = new Article();
        a7.setLibelle("Dés en mousse");
        a7.setDescriptif("Modèle exclusif spécialement dédié aux rétroviseurs de Faucon Millenium. Couleur arc en ciel");
        a7.setPrix(2);
        em.persist(a7);
        
        Facture f1 = new Facture();
        f1.setClient(client1);
        em.persist(f1);
        em.persist(newLigneFacture(f1, a1, 2));
        em.persist(newLigneFacture(f1, a2, 1));

        Facture f2 = new Facture();
        f2.setClient(client2);
        em.persist(f2);
        em.persist(newLigneFacture(f2, a1, 10));

        Facture f3 = new Facture();
        f3.setClient(client1);
        em.persist(f3);
        em.persist(newLigneFacture(f3, a3, 1));
        
        Facture f4 = new Facture();
        f4.setClient(client4);
        em.persist(f4);
        em.persist(newLigneFacture(f4, a4, 1));
        em.persist(newLigneFacture(f4, a5, 2));
        em.persist(newLigneFacture(f4, a6, 1));
        em.persist(newLigneFacture(f4, a7, 1));

    }


    private LigneFacture newLigneFacture(Facture f, Article a1, int quantite) {
        LigneFacture lf1 = new LigneFacture();
        lf1.setArticle(a1);
        lf1.setQuantite(quantite);
        lf1.setFacture(f);
        return lf1;
    }


    private Client newClient(String nom, String prenom, LocalDate dateNaissance) {
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setDateNaissance(dateNaissance);
        return client;
    }

}
