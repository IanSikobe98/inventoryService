package com.shoe.inventoryService.repository;

import com.shoe.inventoryService.entity.Status;
import com.shoe.inventoryService.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginLogRepo extends JpaRepository<UserLoginLog,Integer> {
    UserLoginLog findDistinctByUser_UserIdAndStatus(Integer userId, Status status);
    UserLoginLog findDistinctByUser_UserIdAndStatusIn(Integer userId, List<Status> status);
}
