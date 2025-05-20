package com.smart.config;
import com.smart.smartContactManager.entity.User;
import com.smart.smartContactManager.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetching user from database
        User user = userRepository.getUserByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("Could not found user !");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        return customUserDetails;
    }
}
