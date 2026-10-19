package com.shoe.inventoryService.repository;

import com.shoe.inventoryService.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePrivilegeRepo extends JpaRepository<RolePrivilege, Integer> {
    List<RolePrivilege> findByRole_RoleId(Integer roleId);
}