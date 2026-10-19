package com.shoe.inventoryService.dto.response;



import com.shoe.inventoryService.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {
    private User user;
    private List<String> usersPerm;
    private String role;
}
