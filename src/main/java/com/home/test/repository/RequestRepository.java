package com.home.test.repository;

import com.home.test.entity.RequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<RequestEntity, Long> {

    List<RequestEntity> findAllByUserId(long userId, Pageable pageable);

    long countByUserId(long userId);
}
