package br.com.erudio.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public List<Person> findAll(){
		logger.info("Finding all persons");
		return repository.findAll();
	}

	public Person findById(Long id) {
		logger.info("Finding one person");
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Leandro");
		person.setLastName("Costa");
		person.setAdress("Uberlândia - Minas Gerais - Brasil");
		person.setGender("Male");
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
	}
	
	public Person create(Person person) {
		logger.info("Creating one person");
		return repository.save(person);
	}
	
	public Person update(Person person) {
		logger.info("Updating one person");
		Person pessoa = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		
		pessoa.setFirstName(person.getFirstName());
		pessoa.setLastName(person.getLastName());
		pessoa.setAdress(person.getAdress());
		pessoa.setGender(person.getGender());
		
		return repository.save(person);
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person");
		Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		repository.delete(person);
	}
	
	
}
