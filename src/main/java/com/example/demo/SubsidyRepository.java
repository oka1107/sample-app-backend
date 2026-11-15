package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Client;

public interface SubsidyRepository extends JpaRepository<Client, Long> {

}
