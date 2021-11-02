package com.nj.junitmockapplication;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<book, Long> {
}
