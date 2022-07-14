package br.com.erudio.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public List<PersonVO> findAll(){
		logger.info("Finding all persons");
		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		
		Person person =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		return DozerMapper.parseObject(person, PersonVO.class);
	}
	
	public PersonVO create(PersonVO person) {
		logger.info("Creating one person");
		Person entity = DozerMapper.parseObject(person, Person.class);
		PersonVO vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Updating one person");
		Person pessoa = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		
		pessoa.setFirstName(person.getFirstName());
		pessoa.setLastName(person.getLastName());
		pessoa.setAdress(person.getAdress());
		pessoa.setGender(person.getGender());
		
		PersonVO vo = DozerMapper.parseObject(repository.save(pessoa), PersonVO.class);
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person");
		Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		repository.delete(person);
	}
	
	
}
