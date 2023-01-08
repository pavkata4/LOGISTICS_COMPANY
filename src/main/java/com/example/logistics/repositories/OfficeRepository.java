package com.example.logistics.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.logistics.domain.entities.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByAddress(String address);
}
