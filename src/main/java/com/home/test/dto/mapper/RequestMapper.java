package com.home.test.dto.mapper;

import com.home.test.dto.RequestRecord;
import com.home.test.entity.RequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "userFirstName", source = "user.firstName")
    @Mapping(target = "userLastName", source = "user.lastName")
    @Mapping(target = "userId", source = "user.id")
    RequestRecord toRecord(RequestEntity entity);

    @Mapping(target = "user", ignore = true)
    RequestEntity toEntity(RequestRecord record);

    default long stringToLong(String s){
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        return Long.parseLong(s);
    }
}
