package com.home.test.dto.mapper;

import com.home.test.dto.UserRecord;
import com.home.test.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserRecord toRecord(UserEntity entity);

    @Mapping(target = "requests", ignore = true)
    UserEntity toEntity(UserRecord record);

    default long stringToLong(String s){
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        return Long.parseLong(s);
    }
}
