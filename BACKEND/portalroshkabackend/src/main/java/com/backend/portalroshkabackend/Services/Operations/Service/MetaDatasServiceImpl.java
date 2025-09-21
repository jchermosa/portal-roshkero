package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.ClientesResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.MetaDatasDto;
import com.backend.portalroshkabackend.DTO.Operationes.TecnologiasResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Repositories.ClientesRepository;
import com.backend.portalroshkabackend.Repositories.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IMetaDatasService;

@Service
public class MetaDatasServiceImpl implements IMetaDatasService {
        private final TecnologiaRepository tecnologiaRepository;
        private final ClientesRepository clientesRepository;
        private final UserRepository userRepository;

        private Integer idTeamLeader = 3;

        @Autowired
        public MetaDatasServiceImpl(TecnologiaRepository tecnologiaRepository, ClientesRepository clientesRepository,
                        UserRepository userRepository) {
                this.tecnologiaRepository = tecnologiaRepository;
                this.clientesRepository = clientesRepository;
                this.userRepository = userRepository;
        }

        @Override
        public MetaDatasDto getMetaDatas() {
                MetaDatasDto metaDatas = new MetaDatasDto();

                List<ClientesResponseDto> clientes = clientesRepository.findAll()
                                .stream()
                                .map(c -> new ClientesResponseDto(
                                                c.getIdCliente(),
                                                c.getNombre()))
                                .toList();

                List<TecnologiasResponseDto> tecnologias = tecnologiaRepository.findAll()
                                .stream()
                                .map(t -> new TecnologiasResponseDto(
                                                t.getIdTecnologia(),
                                                t.getNombre(),
                                                t.getDescripcion()))
                                .toList();

                List<UsuarioisResponseDto> teamLeaders = userRepository.findAllByIdRolId(idTeamLeader)
                                .stream()
                                .map(u -> new UsuarioisResponseDto(
                                                u.getIdUsuario(),
                                                u.getNombre(),
                                                u.getApellido(),
                                                u.getCorreo(),
                                                u.getDisponibilidad()))
                                .toList();

                metaDatas.setTecnologias(tecnologias);
                metaDatas.setClientes(clientes);
                metaDatas.setTeamLeaders(teamLeaders);
                return metaDatas;
        }
}
