package com.javandl.spring_crud.repository;


import com.javandl.spring_crud.entity.Disk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiskRepository extends JpaRepository<Disk, Integer> {
}