package com.kiet.library.controller;

import com.kiet.library.model.Book;
import com.kiet.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Q2(b): Add a book using @PostMapping and @RequestBody.
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        if (book == null || book.getId() == null) {
            return ResponseEntity.badRequest().body("Book ID is required.");
        }
        if (bookService.addBook(book)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("A book with this ID already exists.");
    }

    // Q2(c): Retrieve all books.
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Q2(c): Retrieve one book by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found with ID: " + id));
    }

    // Q2(d): Update book details using @PutMapping.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id,
                                        @RequestBody Book updatedBook) {
        if (updatedBook == null) {
            return ResponseEntity.badRequest().body("Request body is required.");
        }
        return bookService.updateBook(id, updatedBook)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found with ID: " + id));
    }

    // Q2(e): Delete a book using @DeleteMapping.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.ok("Book deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found with ID: " + id);
    }
}
