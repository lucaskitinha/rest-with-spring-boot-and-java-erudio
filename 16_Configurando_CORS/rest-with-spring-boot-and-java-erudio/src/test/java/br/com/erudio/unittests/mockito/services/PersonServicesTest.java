package br.com.erudio.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import br.com.erudio.services.PersonServices;
import br.com.erudio.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {
	
	MockPerson input;
	
	@InjectMocks
	private PersonServices service;
	
	@Mock
	PersonRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testFindById() throws Exception {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
	
		PersonVO result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAdress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	@Test
	void testCreate() throws Exception {
		Person entity = input.mockEntity(1);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		PersonVO result = service.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAdress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		
	}
	
	@Test
	void testCreateWithNullPerson() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testUpdate() throws Exception {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		PersonVO result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAdress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testUpdateeWithNullPerson() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
	
		service.delete(1L);
	}

	@Test
	void testFindAll() {
		List<Person> list = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(list);
	
		List<PersonVO> people = service.findAll();
		
		assertNotNull(people);
		assertEquals(14, people.size());
		
		PersonVO person1 = people.get(1);
		
		assertNotNull(person1);
		assertNotNull(person1.getKey());
		assertNotNull(person1.getLinks());
		assertTrue(person1.toString().contains("[</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", person1.getAdress());
		assertEquals("First Name Test1", person1.getFirstName());
		assertEquals("Last Name Test1", person1.getLastName());
		assertEquals("Female", person1.getGender());
		
		PersonVO person4 = people.get(4);
		
		assertNotNull(person4);
		assertNotNull(person4.getKey());
		assertNotNull(person4.getLinks());
		assertTrue(person4.toString().contains("[</api/person/v1/4>;rel=\"self\"]"));
		assertEquals("Addres Test4", person4.getAdress());
		assertEquals("First Name Test4", person4.getFirstName());
		assertEquals("Last Name Test4", person4.getLastName());
		assertEquals("Male", person4.getGender());
		
		PersonVO person7 = people.get(7);
		
		assertNotNull(person7);
		assertNotNull(person7.getKey());
		assertNotNull(person7.getLinks());
		assertTrue(person7.toString().contains("[</api/person/v1/7>;rel=\"self\"]"));
		assertEquals("Addres Test7", person7.getAdress());
		assertEquals("First Name Test7", person7.getFirstName());
		assertEquals("Last Name Test7", person7.getLastName());
		assertEquals("Female", person7.getGender());
	}
}
