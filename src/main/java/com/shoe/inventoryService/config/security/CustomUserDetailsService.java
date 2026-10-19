package com.shoe.inventoryService.config.security;





import com.shoe.inventoryService.entity.User;
import com.shoe.inventoryService.service.DatabaseService;
import com.shoe.inventoryService.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final DatabaseService databaseService;
    private final ConstantUtil constantUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = databaseService.getUserByUsername(username,constantUtil);
        if(user == null) throw new UsernameNotFoundException("User not Found");
        return new SecurityUser(user,constantUtil);
    }
}
