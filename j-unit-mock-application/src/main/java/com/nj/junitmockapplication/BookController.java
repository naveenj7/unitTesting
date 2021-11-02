package com.nj.junitmockapplication;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookRepo bookRepo;


    @GetMapping()
    public List<book> getAllBooks(){return bookRepo.findAll();}

    @GetMapping("/{bookId}")
    public book getBookById(@PathVariable(value = "bookId")  Long bookId){

        return bookRepo.findById(bookId).get();
    }

    @PostMapping()
    public book addBook(@RequestBody @Valid book bookRecord){
        return bookRepo.save(bookRecord);
    }

    @PutMapping
    public book modifyBook(@RequestBody @Valid book bookRecord) throws Exception {

       Optional<book> optionalBook = bookRepo.findById(bookRecord.getBookId());

       if(!optionalBook.isPresent()){
           throw new NotFoundException("book with ID" + bookRecord.getBookId() + "not found");
       }

        book existingBook = optionalBook.get();
       existingBook.setBookName(bookRecord.getBookName());
       existingBook.setRating(bookRecord.getRating());

       return bookRepo.save(existingBook);
    }
}
