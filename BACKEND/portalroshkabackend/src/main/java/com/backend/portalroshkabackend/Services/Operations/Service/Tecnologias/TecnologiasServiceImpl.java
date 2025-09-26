package com.backend.portalroshkabackend.Services.Operations.Service.Tecnologias;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Repositories.TecnologiaRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.Tecnologias.ITecnologiasService;

@Service
public class TecnologiasServiceImpl implements ITecnologiasService {

    private final TecnologiaRepository tecnologiaRepository;

    @Autowired
    public TecnologiasServiceImpl(TecnologiaRepository tecnologiaRepository) {
        this.tecnologiaRepository = tecnologiaRepository;
    }

    @Override
    public Page<TecnologiasResponseDto> getAllTecnologias(Pageable pageable, String sortBy) {
        Page<Tecnologias> page = tecnologiaRepository.findAll(pageable);
        List<TecnologiasResponseDto> dtoList = page.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    @Override
    public TecnologiasResponseDto getTecnologiaById(Integer id) {
        Tecnologias t = tecnologiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnologia not found"));
        return toDto(t);
    }

    @Override
    public TecnologiasResponseDto createTecnologia(TecnologiasRequestDto dto) {
        Tecnologias t = new Tecnologias();
        t.setNombre(dto.getNombre());
        t.setDescripcion(dto.getDescripcion());
        t.setFechaCreacion(LocalDateTime.now());
        tecnologiaRepository.save(t);
        return toDto(t);
    }

    @Override
    public TecnologiasResponseDto updateTecnologia(Integer id, TecnologiasRequestDto dto) {
        Tecnologias t = tecnologiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnologia not found"));
        if (dto.getNombre() != null)
            t.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null)
            t.setDescripcion(dto.getDescripcion());
        tecnologiaRepository.save(t);
        return toDto(t);
    }

    @Override
    public void deleteTecnologia(Integer id) {
        Tecnologias t = tecnologiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnologia not found"));
        tecnologiaRepository.delete(t);
    }

    private TecnologiasResponseDto toDto(Tecnologias t) {
        return new TecnologiasResponseDto(
                t.getIdTecnologia(),
                t.getNombre(),
                t.getDescripcion(),
                t.getFechaCreacion());
    }
}
