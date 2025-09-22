package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.MetaDatasDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;

public interface IMetaDatasService {
    MetaDatasDto getMetaDatas();
    List<UsuarioisResponseDto> getAllUsers();
}
