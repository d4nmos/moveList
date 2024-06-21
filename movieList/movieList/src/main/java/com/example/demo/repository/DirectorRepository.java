package com.example.demo.repository;

import com.example.demo.models.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.DigestException;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Director findByName(String name);
}
