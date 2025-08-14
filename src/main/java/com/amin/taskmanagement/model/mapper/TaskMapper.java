package com.amin.taskmanagement.model.mapper;


import com.amin.taskmanagement.model.Task;
import com.amin.taskmanagement.model.dto.TaskRequest;
import com.amin.taskmanagement.model.dto.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "status", expression = "java(com.amin.taskmanagement.model.Task.Status.valueOf(dto.getStatus().toUpperCase()))")
    Task toEntity(TaskRequest dto);

    TaskResponse toResponse(Task entity);

    void updateEntityFromDto(TaskRequest dto, @MappingTarget Task entity);

}
