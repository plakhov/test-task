package com.home.test.service;

import com.home.test.dto.RequestRecord;
import com.home.test.dto.UserRecord;
import com.home.test.dto.mapper.RequestMapper;
import com.home.test.entity.RequestEntity;
import com.home.test.entity.Role;
import com.home.test.exception.NotFoundException;
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
    public Optional<RequestRecord> create(RequestRecord record) {
        RequestEntity requestEntity = requestMapper.toEntity(record);
        try {
            requestEntity.setUser(userRepository.findById(record.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User not found, id=" + record.getUserId())));
            requestEntity = requestRepository.save(requestEntity);
        } catch (Exception e) {
            LOG.error("Got exception while create request", e);
            return Optional.empty();
        }
        return Optional.of(requestMapper.toRecord(requestEntity));
    }

    @Override
    @Transactional
    public Optional<RequestRecord> update(RequestRecord record) {
        RequestEntity oldEntity;
        try {
            oldEntity = requestRepository.findById(Long.parseLong(record.getId()))
                    .orElseThrow(() -> new IllegalStateException("Request not found, id=" + record.getId()));
            RequestEntity newEntity = requestMapper.toEntity(record);
            oldEntity.setName(newEntity.getName());
            oldEntity.setDescription(newEntity.getDescription());
            requestRepository.save(oldEntity);
        } catch (Exception e) {
            LOG.error("Got exception while update request", e);
            return Optional.empty();
        }
        return Optional.of(requestMapper.toRecord(oldEntity));
    }

    @Override
    @Transactional
    public Optional<RequestRecord> getById(long id, UserRecord userRecord) {
        Optional<RequestEntity> entity = requestRepository.findById(id);
        entity.ifPresent(requestEntity -> {
            if (userRecord != null
                    && userRecord.getRole() == Role.ROLE_USER
                    && requestEntity.getUser().getId() != Long.parseLong(userRecord.getId())) {
                throw new NotFoundException();
            }
        });
        return entity.map(requestMapper::toRecord);
    }

    @Override
    @Transactional
    public List<RequestRecord> getAll(Pageable pageable) {
        return requestRepository.findAll(pageable).stream()
                .map(requestMapper::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
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
    public boolean remove(long id, UserRecord userRecord) {
        requestRepository.findById(id).ifPresent(requestEntity -> {
            if (userRecord.getRole() == Role.ROLE_USER && requestEntity.getUser().getId() != Long.parseLong(userRecord.getId())) {
                throw new NotFoundException();
            }
        });
        try {
            requestRepository.deleteById(id);
        } catch (Exception e) {
            LOG.error("Got exception while remove request", e);
            return false;
        }
        return true;
    }
}
