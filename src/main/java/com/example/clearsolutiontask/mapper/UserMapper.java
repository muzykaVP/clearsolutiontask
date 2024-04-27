package com.example.clearsolutiontask.mapper;

import com.example.clearsolutiontask.dto.UserRequestDTO;
import com.example.clearsolutiontask.dto.UserResponseDTO;
import com.example.clearsolutiontask.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserRequestDTO userToUserRequestDTO(User user);
    User userRequestDTOToUser(UserRequestDTO userDTO);
    UserResponseDTO userToUserResponseDTO(User user);
    User userResponseDTOToUser(UserResponseDTO userDTO);
}
