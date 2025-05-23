package com.smart.smartContactManager.repo;

import com.smart.smartContactManager.entity.Contact;
import com.smart.smartContactManager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    //pagination
    @Query("from Contact as c where c.user.id =:userId")
    //
   public Page<Contact> findContactsByUser(@Param("userId") int UserId, Pageable pageable);
    public List<Contact> findByNameContainingAndUser(String name, User user);

}
