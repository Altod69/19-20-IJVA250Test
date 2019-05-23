package com.example.demo.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entity.Client;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryTest {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void save() {
		Client client  = new Client();
		client.setNom("Alex");
		client.setPrenom("Theo");
		
		clientRepository.save(client);
		
		List<Client> allClients = clientRepository.findAll();
		Assertions.assertThat(clientRepository.findAll()).hasSize(1);
		Assertions.assertThat(allClients.get(0).getId()).isNotNull());
		Assertions.assertThat(allClients.get(0).getPrenom()).isEqualTo("Alex");
	}
}
