package com.bulpros.formio.repository;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.User;

import java.util.List;

public interface UserProfileRepository {
    List<ResourceDto> getUserProfilesByUserIds(String projectId, User user, List<String> userIds);
    List<ResourceDto> getUserProfilesByUserNameRegex(String projectId, User user, String username);
}
