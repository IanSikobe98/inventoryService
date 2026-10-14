package com.shoe.inventoryService.repository;

import com.shoe.inventoryService.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}
