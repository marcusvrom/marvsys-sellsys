package com.marvsys.marvsys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marvsys.marvsys.entities.Combo;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long>{

}
