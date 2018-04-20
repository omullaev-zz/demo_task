package com.app.repository;

import com.app.UrlForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<UrlForm, Long> {
    @Query("select u from UrlForm u where u.rawValue = ?1")
    UrlForm findByRawValue(String rawValue);
}
