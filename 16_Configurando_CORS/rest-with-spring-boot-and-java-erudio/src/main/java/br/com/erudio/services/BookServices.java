package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BookRepository;

@Service
public class BookServices {
	
	//private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired
	BookRepository repository;
	
	public List<BookVO> findAll(){
		logger.info("Finding all books");
		List<BookVO> books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
		books.stream().forEach(p -> {
			try {
				p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return books;
	}

	public BookVO findById(Long id) throws Exception {
		logger.info("Finding one book");
		
		Book book =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		BookVO vo = DozerMapper.parseObject(book, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public BookVO create(BookVO book) throws Exception {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one book");
		Book entity = DozerMapper.parseObject(book, Book.class);
		BookVO vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public BookVO update(BookVO book) throws Exception {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one book");
		Book entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		BookVO vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one book");
		Book book = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this id!!"));
		repository.delete(book);
	}
	
	
}
