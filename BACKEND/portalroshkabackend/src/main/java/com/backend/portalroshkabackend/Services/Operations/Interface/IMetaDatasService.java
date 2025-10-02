package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.MetaDatasDto;

public interface IMetaDatasService {
    MetaDatasDto getMetaDatas();
    List<UsuarioisResponseDto> getAllUsers();
}
