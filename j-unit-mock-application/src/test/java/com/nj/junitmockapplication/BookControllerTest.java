package com.nj.junitmockapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookController bookController;

    book book_record1 = new book(1l,"Mookajjiya Kanasugalu", 5);
    book book_record2 = new book(2l, "manadha maathu", 5);
    book book_record3 = new book(3l,"manku thimmana kagga", 4);


    @Before
    public  void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBooks_success() throws Exception{
        List<book> records = new ArrayList<>(Arrays.asList(book_record1, book_record2, book_record3));

        Mockito.when(bookRepo.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].bookName", is("manku thimmana kagga")));
    }

    @Test
    public void getBookById() throws Exception{

        Mockito.when(bookRepo.findById(book_record1.getBookId())).thenReturn(Optional.of(book_record1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.bookName", is("Mookajjiya Kanasugalu")));

    }

    @Test
    public  void  addBook_success() throws Exception{

        book record = book.builder()
                .bookId(4l)
                .bookName("ratnan prapancha")
                .rating(5)
                .build();

        Mockito.when(bookRepo.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.bookName", is("ratnan prapancha")));

    }

    @Test
    public void updateBook() throws Exception{

        book updatedRecord = book.builder()
                .bookId(1l)
                .bookName("Updating the First book")
                .rating(3)
                .build();

        Mockito.when(bookRepo.findById(book_record1.getBookId())).thenReturn(Optional.of(book_record1));

        Mockito.when(bookRepo.save(updatedRecord)).thenReturn(updatedRecord);
        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.bookName", is("Updating the First book")));

    }

}
