package com.home.test.service;

import com.home.test.dto.UserRecord;
import com.home.test.dto.mapper.UserMapper;
import com.home.test.entity.UserEntity;
import com.home.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.userMapper = UserMapper.INSTANCE;
    }

    @Override
    public boolean create(UserRecord record) {
        try {
            UserEntity userEntity = userMapper.toEntity(record);
            userEntity.setPasswordHash(bCryptPasswordEncoder.encode(record.getPassword()));
            userRepository.save(userEntity);
        } catch (Exception e) {
            LOG.error("Got exception while save user", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean update(UserRecord record) {
        try {
            UserEntity oldEntity = userRepository.findByLogin(record.getLogin()).orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));
            UserEntity newEntity = userMapper.toEntity(record);
            oldEntity.setFirstName(newEntity.getFirstName());
            oldEntity.setLastName(newEntity.getLastName());
            oldEntity.setLogin(newEntity.getLogin());
            oldEntity.setRole(newEntity.getRole());
            if (!StringUtils.isEmpty(record.getPasswordHash())) {
                oldEntity.setPasswordHash(bCryptPasswordEncoder.encode(record.getPassword()));
            }
            userRepository.save(oldEntity);
        } catch (Exception e) {
            LOG.error("Got exception while update user", e);
            return false;
        }
        return true;
    }

    @Override
    public List<UserRecord> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(userMapper::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserRecord> getById(long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toRecord);
    }

    @Override
    public boolean remove(long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            LOG.error("Got exception while remove user", e);
            return false;
        }
        return true;
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).map(userMapper::toRecord).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
