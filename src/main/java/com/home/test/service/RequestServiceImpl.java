package com.home.test.service;

import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import com.home.test.dto.mapper.RequestMapper;
import com.home.test.entity.RequestEntity;
import com.home.test.repository.RequestRepository;
import com.home.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    private static final Logger LOG = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    public RequestServiceImpl(RequestRepository requestRepository,
                              UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.requestMapper = RequestMapper.INSTANCE;
    }

    @Override
    @Transactional
    public boolean create(RequestRecord record) {
        try {
            RequestEntity requestEntity = requestMapper.toEntity(record);
            requestEntity.setUser(userRepository.findById(record.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User not found, id=" + record.getUserId())));
            requestRepository.save(requestEntity);
        } catch (Exception e) {
            LOG.error("Got exception while create request", e);
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean update(RequestRecord record) {
        try {
            RequestEntity newEntity = requestMapper.toEntity(record);
            RequestEntity oldEntity = requestRepository.findById(Long.parseLong(record.getId()))
                    .orElseThrow(() -> new IllegalStateException("Request not found, id=" + record.getId()));
            oldEntity.setName(newEntity.getName());
            oldEntity.setDescription(newEntity.getDescription());
            requestRepository.save(oldEntity);
        } catch (Exception e) {
            LOG.error("Got exception while update request", e);
            return false;
        }
        return true;
    }

    @Override
    public Optional<RequestRecord> getById(long id) {
        return requestRepository.findById(id).map(requestMapper::toRecord);
    }

    @Override
    public List<RequestRecord> getAll(Pageable pageable) {
        return requestRepository.findAll(pageable).stream()
                .map(requestMapper::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestRecord> getByUser(UserRecord record, Pageable pageable) {
        return requestRepository.findAllByUserId(Long.parseLong(record.getId()), pageable).stream()
                .map(requestMapper::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return requestRepository.count();
    }

    @Override
    public long countByUserId(long userId) {
        return requestRepository.countByUserId(userId);
    }

    @Override
    public boolean remove(long id) {
        try {
            requestRepository.deleteById(id);
        } catch (Exception e) {
            LOG.error("Got exception while remove request", e);
            return false;
        }
        return true;
    }
}