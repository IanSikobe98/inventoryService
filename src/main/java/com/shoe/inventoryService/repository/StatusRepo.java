package com.shoe.inventoryService.repository;



import com.shoe.inventoryService.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<Status, Integer> {
}
