// package com.backend.portalroshkabackend.Services.SysAdmin;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.backend.portalroshkabackend.DTO.DeviceRequestDto;
// import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
// import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
// import com.backend.portalroshkabackend.Repositories.DeviceRequestRepository;

// import jakarta.transaction.Transactional;

// @Service
// public class DeviceRequest {

//     @Autowired
//     private DeviceRequestRepository deviceRequestRepository;

//     DeviceRequest(DeviceRequestRepository deviceRequestRepository) {
//         this.deviceRequestRepository = deviceRequestRepository;
//     }


//     @Transactional
//     public DeviceRequestDto acceptRequest(Integer idRequest){
//         Optional<SolicitudDispositivos> solicitudOp = deviceRequestRepository.findById(idRequest);
//         if (solicitudOp.isEmpty()) {
//             throw new RuntimeException("Solicitud no encontrada con ID: " + idRequest);
//         }

//         SolicitudDispositivos solicitud = solicitudOp.get();

//         if(solicitud.getAprobacionAdmin()) {
//             throw new RuntimeException("La solicitud ya ha sido aprobada.");
//         }

//         if(solicitud.getEstado() != EstadoSolicitudEnum.P) {
//             throw new RuntimeException("La solicitud ya fue procesada.");
//         }
//         solicitud.setAprobacionAdmin(true);
//         solicitud.setEstado(EstadoSolicitudEnum.A);
//         deviceRequestRepository.save(solicitud);
//         return convertToDto(solicitud);
//     }

//     @Transactional
//     public DeviceRequestDto rejectRequest(Integer idRequest) {
//         Optional<SolicitudDispositivos> solicitudOp = deviceRequestRepository.findById(idRequest);
//         if (solicitudOp.isEmpty()) {
//             throw new RuntimeException("Solicitud no encontrada con ID: " + idRequest);
//         }

//         SolicitudDispositivos solicitud = solicitudOp.get();

//         if(solicitud.getAprobacionAdmin()) {
//             throw new RuntimeException("La solicitud ya ha sido aprobada.");
//         }

//         if(solicitud.getEstado() != EstadoSolicitudEnum.P) {
//             throw new RuntimeException("La solicitud ya fue procesada.");
//         }
//         solicitud.setAprobacionAdmin(false);
//         solicitud.setEstado(EstadoSolicitudEnum.R);
//         deviceRequestRepository.save(solicitud);
//         return convertToDto(solicitud);
//     }

//     /*
//     @Transactional
//     public DeviceRequestDto rejectRequest(int idRequest, @RequestBody DeviceRequestDto deviceRequestDto) {
//         SolicitudDispositivos solicitud = deviceRequestRepository.findById(idRequest).orElse(null);
//         solicitud.setAprobacionAdmin(deviceRequestDto.isAprobacionAdmin());
//         solicitud.setEstado(deviceRequestDto.getEstado());
//         deviceRequestRepository.save(solicitud);
//         return deviceRequestDto;
//     }

//     */
//     private DeviceRequestDto convertToDto(SolicitudDispositivos solicitud) {
//         DeviceRequestDto dto = new DeviceRequestDto();
//         dto.setFechaInicio(solicitud.getFechaInicio());
//         dto.setCantDias(solicitud.getCantidadDias());
//         dto.setAprobacionAdmin(solicitud.getAprobacionAdmin());
//         dto.setEstado(solicitud.getEstado());
//         dto.setComentario(solicitud.getComentario());
//         dto.setIdUsuario(solicitud.getUsuario().getIdUsuario());
//         dto.setIdTipoDispositivo(solicitud.getTipoDispositivo().getIdTipoDispositivo());
//         return dto;
//     }
// }