package com.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.driver.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query(value="select id from country where country_name= :country ;",nativeQuery = true)
    Integer findByName(String country);
    @Query(value="select id from country where countryName=:country and serviceProvider_id =:id;",nativeQuery = true)
    Integer isAlreadyPresent(String country,Integer id);
}
