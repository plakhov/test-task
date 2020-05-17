package com.home.test.service;

import com.home.test.dto.UserRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    boolean create(UserRecord record);

    boolean update(UserRecord record);

    List<UserRecord> getAll(Pageable pageable);

    Optional<UserRecord> getById(long userId);

    boolean remove(long id);

    long count();
}
