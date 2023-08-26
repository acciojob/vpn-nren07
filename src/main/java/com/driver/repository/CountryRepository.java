package com.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.driver.model.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}
