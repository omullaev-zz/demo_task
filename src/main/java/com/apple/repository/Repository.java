package com.apple.repository;

import com.apple.PersonForm;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<PersonForm, Long> {
}
