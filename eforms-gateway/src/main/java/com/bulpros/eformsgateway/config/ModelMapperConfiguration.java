package com.bulpros.eformsgateway.config;

import com.bulpros.eformsgateway.process.repository.dto.HistoryTaskDto;
import com.bulpros.eformsgateway.process.repository.dto.TaskDto;
import com.bulpros.eformsgateway.process.web.dto.HistoryTaskResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TaskResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(TaskDto.class, TaskResponseDto.class);
        modelMapper.createTypeMap(HistoryTaskDto.class, HistoryTaskResponseDto.class);

        return modelMapper;
    }
}