package br.com.erudio.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.erudio.controllers.PersonController;
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
		List<PersonVO> persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		persons.stream().forEach(p -> {
			try {
				p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return persons;
	}

	public PersonVO findById(Long id) throws Exception {
		logger.info("Finding one person");
		
		Person person =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		PersonVO vo = DozerMapper.parseObject(person, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public PersonVO create(PersonVO person) throws Exception {
		logger.info("Creating one person");
		Person entity = DozerMapper.parseObject(person, Person.class);
		PersonVO vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVO update(PersonVO person) throws Exception {
		logger.info("Updating one person");
		Person pessoa = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		
		pessoa.setFirstName(person.getFirstName());
		pessoa.setLastName(person.getLastName());
		pessoa.setAdress(person.getAdress());
		pessoa.setGender(person.getGender());
		
		PersonVO vo = DozerMapper.parseObject(repository.save(pessoa), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person");
		Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		repository.delete(person);
	}
	
	
}
