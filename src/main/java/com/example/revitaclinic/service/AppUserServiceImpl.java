package com.example.revitaclinic.service;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepo;

    public AppUserServiceImpl(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public AppUser upsertUser(UUID keycloakUserId, String phone) {
        AppUser user = userRepo.findById(keycloakUserId)
                .orElseGet(() -> {
                    AppUser u = new AppUser();
                    u.setKeycloakUserId(keycloakUserId);
                    return u;
                });
        user.setPhone(phone);
        return userRepo.save(user);
    }
}