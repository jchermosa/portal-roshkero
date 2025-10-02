package com.backend.portalroshkabackend.Services.Operations.Service.Equipo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.DiasLaboralDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquipoDiaUbicacionResponceDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto;
import com.backend.portalroshkabackend.Models.DiaLaboral;
import com.backend.portalroshkabackend.Models.EquipoDiaUbicacion;
import com.backend.portalroshkabackend.Repositories.DiasLaboralRepository;
import com.backend.portalroshkabackend.Repositories.EquipoDiaUbicacionRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.Equipo.IEquipoDiaUbicacionService;

@Service
public class EquipoDiaUbicacionServiceImpl implements IEquipoDiaUbicacionService {

        @Autowired
        private DiasLaboralRepository diasLaboralRepository;
        @Autowired
        private EquipoDiaUbicacionRepository equipoDiaUbicacionRepository;

        @Override
        public List<EquipoDiaUbicacionResponceDto> EquipoDiaUbicacion(Integer idEquipo) {

                List<DiaLaboral> dias = diasLaboralRepository.findAll();

                List<EquipoDiaUbicacion> asignacionesDU = equipoDiaUbicacionRepository
                                .findAllByEquipo_IdEquipo(idEquipo);

                // для быстрого поиска делаем map
                Map<Integer, EquipoDiaUbicacion> asignacionesMap = asignacionesDU.stream()
                                .collect(Collectors.toMap(
                                                a -> a.getDiaLaboral().getIdDiaLaboral(),
                                                a -> a));

                List<EquipoDiaUbicacionResponceDto> dtoList = dias.stream()
                                .map(dia -> {
                                        EquipoDiaUbicacion asignacion = asignacionesMap.get(dia.getIdDiaLaboral());

                                        if (asignacion != null) {
                                                // have asignacion
                                                return new EquipoDiaUbicacionResponceDto(
                                                                new DiasLaboralDto(dia.getIdDiaLaboral(),
                                                                                dia.getNombreDia()),
                                                                new UbicacionDto(
                                                                                asignacion.getUbicacion()
                                                                                                .getIdUbicacion(),
                                                                                asignacion.getUbicacion().getNombre()));
                                        } else {
                                                // not have asignacion → ubicacion = null
                                                return new EquipoDiaUbicacionResponceDto(
                                                                new DiasLaboralDto(dia.getIdDiaLaboral(),
                                                                                dia.getNombreDia()),
                                                                null);
                                        }
                                })
                                .toList();
                return dtoList;
        }
}
