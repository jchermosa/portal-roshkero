package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;
import com.backend.portalroshkabackend.DTO.th.self.*;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.EmployeeValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThSelfServiceImpl implements IThSelfService {
    private final BeneficiosRepository beneficiosRepository;
    private final SolicitudTHTipoRepository solicitudTHTipoRepository;
    private final TipoDispositivoRepository tipoDispositivoRepository;
    private final PermisosRepository permisosRepository;
    private final UserRepository userRepository;
    private final SolicitudRepository solicitudRepository;

    private final EmployeeValidator employeeValidator;

    public ThSelfServiceImpl(BeneficiosRepository beneficiosRepository,
                             SolicitudTHTipoRepository solicitudTHTipoRepository,
                             TipoDispositivoRepository tipoDispositivoRepository,
                             PermisosRepository permisosRepository,
                             UserRepository userRepository,
                             SolicitudRepository solicitudRepository,
                             EmployeeValidator employeeValidator
    ) {
        this.beneficiosRepository = beneficiosRepository;
        this.solicitudTHTipoRepository = solicitudTHTipoRepository;
        this.tipoDispositivoRepository = tipoDispositivoRepository;
        this.permisosRepository = permisosRepository;
        this.solicitudRepository = solicitudRepository;
        this.userRepository = userRepository;

        this.employeeValidator = employeeValidator;
    }

    @Transactional
    @Override
    public EmailUpdatedDto updateEmail(int id, String newEmail) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        employeeValidator.validateUniqueEmail(newEmail, id); // Si el correo ya esta asginado a otro usuario, lanza excepcion

        user.setCorreo(newEmail);

        Usuario savedUser = SaveManager.saveEntity(() -> userRepository.save(user), "Error al guardar el usuario: ");// Este metodo recibe una funcion como primer parametro, y mensaje de error como segundo parametro, dentro de saveEntity se maneja el try/catch por si ocurre error al guardar la entidad.

        return AutoMap.toEmailUpdatedDto(savedUser);
    }

    @Transactional
    @Override
    public PhoneUpdatedDto updatePhone(int id, String newPhone) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        employeeValidator.validateUniquePhone(newPhone, id);

        user.setTelefono(newPhone);

        Usuario savedUser = SaveManager.saveEntity(() -> userRepository.save(user), "Error al guardar el usuario: ");

        return AutoMap.toPhoneUpdatedDto(savedUser);
    }

    @Transactional
    @Override
    public RequestResponseDto sendRequest(int id, SendSolicitudDto sendSolicitudDto) {
        Solicitud solicitud = new Solicitud();

        System.out.println(sendSolicitudDto.getFechaInicio());

        AutoMap.toSolicitudesThFromSendDto(solicitud, sendSolicitudDto);

        System.out.println(solicitud.getFechaInicio());

        SaveManager.saveEntity(() -> solicitudRepository.save(solicitud), "Error al guardar la solicitud: ");

        RequestResponseDto responseDto = new RequestResponseDto();
        responseDto.setId(solicitud.getIdSolicitud());
        responseDto.setMessage("Solicitud enviada con exito");

        System.out.println(solicitud.getFechaInicio());

        return responseDto;
    }

    @Override
    public RequestResponseDto updateRequest(int idSolicitud, UpdateSolicitudDto updateSolicitudDto) {
        Solicitud request  = solicitudRepository.findById(idSolicitud).orElseThrow(() -> new RequestNotFoundException(idSolicitud));

        AutoMap.toSolicitudesThFromUpdateSolicitudDto(request, updateSolicitudDto);

        SaveManager.saveEntity(() -> solicitudRepository.save(request), "Error al actualizar la solicitud: ");

        RequestResponseDto responseDto = new RequestResponseDto();
        responseDto.setId(request.getIdSolicitud());
        responseDto.setMessage("Solicitud actualizada con exito");

        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BenefitsTypesResponseDto> getAllBenefitsTypes() {
        return beneficiosRepository.findAll().stream().map(AutoMap::toBenefitsResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DevicesTypesResponseDto> getAllDevicesTypes() {
        return tipoDispositivoRepository.findAll().stream().map(AutoMap::toDevicesTypesResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PermissionsTypesResponseDto> getAllPermissionsTypes() {
        return permisosRepository.findAll().stream().map(AutoMap::toPermissionsTypesResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SolicitudTHTipoResponseDto> getAllRequestTypes() {
        return solicitudTHTipoRepository.findAll().stream().map(AutoMap::toSolicitudTHTipoResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MisSolicitudesResponseDto> getAllSelfRequests(int idUsuario, Pageable pageable) {
        return solicitudRepository.findAllByUsuario_idUsuario(idUsuario, pageable).map(AutoMap::toMisSolicitudesResponseDto);
    }

    @Override
    public SolicitudEspecificaResponseDto getRequestById(int idUsuario ,int idSolicitud) {
        Solicitud request = solicitudRepository.findByUsuario_IdUsuarioAndIdSolicitud(idUsuario, idSolicitud);

        if (request == null) throw new RequestNotFoundException(idSolicitud);

        return AutoMap.toSolicitudEspecificaResponseDto(request);
    }


}
