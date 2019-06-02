package com.questionbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.questionbank.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
