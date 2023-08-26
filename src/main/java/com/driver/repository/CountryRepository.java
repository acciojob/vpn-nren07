package com.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.driver.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query(value="select id from country where countryName=:name;",nativeQuery = true)
    Integer findByName(String name);
}
