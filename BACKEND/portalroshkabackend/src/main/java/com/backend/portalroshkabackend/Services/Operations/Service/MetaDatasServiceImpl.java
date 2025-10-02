package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.ClientesResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.MetaDatasDto;
import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Repositories.OP.ClientesRepository;
import com.backend.portalroshkabackend.Repositories.OP.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IMetaDatasService;
import com.backend.portalroshkabackend.tools.mapper.MetaDatasMapper;

@Service
public class MetaDatasServiceImpl implements IMetaDatasService {
        @Autowired
        private TecnologiaRepository tecnologiaRepository;
        @Autowired
        private ClientesRepository clientesRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private CargosRepository cargoRepository;
        @Autowired
        private MetaDatasMapper metaDatasMapper;

        @Override
        public MetaDatasDto getMetaDatas() {
                MetaDatasDto metaDatas = new MetaDatasDto();

                List<ClientesResponseDto> clientes = clientesRepository.findAll()
                                .stream()
                                .map(metaDatasMapper::toClienteDto)
                                .toList();

                List<TecnologiasResponseDto> tecnologias = tecnologiaRepository.findAll()
                                .stream()
                                .map(metaDatasMapper::toTecnologiaDto)
                                .toList();
                Integer idTechLead = cargoRepository.findByNombre("Tech Lead")
                                .map(Cargos::getIdCargo)
                                .orElseThrow(() -> new RuntimeException("Cargo 'Tech Lead' not found"));
                List<UsuarioisResponseDto> teamLeaders = userRepository.findAllByCargo_IdCargo(idTechLead)
                                .stream()
                                .map(metaDatasMapper::toUsuarioDto)
                                .toList();

                metaDatas.setTecnologias(tecnologias);
                metaDatas.setClientes(clientes);
                metaDatas.setTeamLeaders(teamLeaders);
                return metaDatas;
        }

        @Override
        public List<UsuarioisResponseDto> getAllUsers() {
                return userRepository.findAllUsuariosByRol4()
                                .stream()
                                .map(metaDatasMapper::toUsuarioDto)
                                .toList();
        }
}
