package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.SpecialtyMapper;
import com.example.revitaclinic.model.Specialty;
import com.example.revitaclinic.repository.SpecialtyRepository;
import com.example.revitaclinic.service.SpecialtyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository repo;
    private final SpecialtyMapper mapper;

    public SpecialtyServiceImpl(SpecialtyRepository repo,
                                SpecialtyMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public SpecialtyDto create(CreateSpecialtyDto dto) {
        Specialty spec = mapper.toEntity(dto);
        repo.save(spec);
        return mapper.toDto(spec);
    }

    @Override
    public SpecialtyDto findById(Integer id) {
        Specialty spec = getEntity(id);
        return mapper.toDto(spec);
    }

    @Override
    public List<SpecialtyDto> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public SpecialtyDto update(Integer id, UpdateSpecialtyDto dto) {
        Specialty spec = getEntity(id);
        mapper.updateEntityFromDto(dto, spec);
        repo.save(spec);
        return mapper.toDto(spec);
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Specialty not found: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public Specialty getEntity(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found: " + id));
    }
}