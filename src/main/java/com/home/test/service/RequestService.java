package com.home.test.service;

import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface RequestService {

    boolean create(RequestRecord record);

    boolean update(RequestRecord record);

    Optional<RequestRecord> getById(long id);

    List<RequestRecord> getAll(Pageable pageable);

    List<RequestRecord> getByUser(UserRecord record, Pageable pageable);

    long count();

    long countByUserId(long userId);

    boolean remove(long id);
}
