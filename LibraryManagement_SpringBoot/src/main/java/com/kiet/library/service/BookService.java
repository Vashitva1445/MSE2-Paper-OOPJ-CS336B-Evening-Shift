package com.kiet.library.service;

import com.kiet.library.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    // ArrayList is the collection used to store book records in memory.
    private final List<Book> books = new ArrayList<>();

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(Integer id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public boolean addBook(Book book) {
        if (book == null || book.getId() == null ||
                getBookById(book.getId()).isPresent()) {
            return false;
        }
        books.add(book);
        return true;
    }

    public Optional<Book> updateBook(Integer id, Book updatedBook) {
        Optional<Book> existing = getBookById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Book book = existing.get();
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setCategory(updatedBook.getCategory());
        book.setAvailableCopies(updatedBook.getAvailableCopies());
        return Optional.of(book);
    }

    public boolean deleteBook(Integer id) {
        return books.removeIf(book -> book.getId().equals(id));
    }
}
