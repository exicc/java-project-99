package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    uses = { JsonNullableMapper.class, ReferenceMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {

    public abstract TaskStatus toEntity(TaskStatusCreateDTO taskStatusDto);

    public abstract TaskStatusDTO toDto(TaskStatus taskStatus);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void partialUpdate(TaskStatusUpdateDTO taskStatusDto, @MappingTarget TaskStatus taskStatus);
}
