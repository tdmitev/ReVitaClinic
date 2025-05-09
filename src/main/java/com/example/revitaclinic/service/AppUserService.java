package com.example.revitaclinic.service;

import com.example.revitaclinic.model.AppUser;

import java.util.UUID;

public interface AppUserService {
    AppUser upsertUser(UUID keycloakUserId, String phone);
}