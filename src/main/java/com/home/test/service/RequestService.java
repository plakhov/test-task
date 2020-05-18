package com.home.test.service;

import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface RequestService {

    Optional<RequestRecord> create(RequestRecord record);

    Optional<RequestRecord> update(RequestRecord record);

    Optional<RequestRecord> getById(long id, UserRecord userRecord);

    List<RequestRecord> getAll(Pageable pageable);

    List<RequestRecord> getByUser(UserRecord record, Pageable pageable);

    long count();

    long countByUserId(long userId);

    boolean remove(long id, UserRecord record);
}
