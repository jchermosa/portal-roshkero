package com.backend.portalroshkabackend.Services.HumanResource.subservices;

import com.backend.portalroshkabackend.Models.BeneficiosAsignados;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoBeneficios;
import com.backend.portalroshkabackend.Repositories.TH.BeneficiosAsignadosRepository;
import com.backend.portalroshkabackend.Repositories.TH.BeneficiosRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.beneficios.BenefitTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service("acceptBenefitService")
public class BenefitServiceImpl implements IAcceptRequestService{
    private final BeneficiosAsignadosRepository beneficiosAsignadosRepository;
    private final BeneficiosRepository beneficiosRepository;
    private final RepositoryService repositoryService;

    @Autowired
    public BenefitServiceImpl(BeneficiosAsignadosRepository beneficiosAsignadosRepository,
                              BeneficiosRepository beneficiosRepository,
                              RepositoryService repositoryService
    ){
        this.beneficiosAsignadosRepository = beneficiosAsignadosRepository;
        this.beneficiosRepository = beneficiosRepository;
        this.repositoryService = repositoryService;
    }

    @Transactional
    @Override
    public void acceptRequest(Solicitud request) {
        BeneficiosAsignados beneficiosAsignados = new BeneficiosAsignados();

        String comentario = request.getComentario();
        Integer idTipoBeneficio = extraerIdTipoBeneficio(comentario);

        if (idTipoBeneficio == null) throw new IllegalArgumentException("No se pudo extraer el tipo de beneficio del comentario.");

        TipoBeneficios tipoBeneficios = repositoryService.findByIdOrThrow(
                beneficiosRepository,
                idTipoBeneficio,
                () -> new BenefitTypeNotFoundException(idTipoBeneficio)
        );

        beneficiosAsignados.setBeneficio(tipoBeneficios);
        beneficiosAsignados.setSolicitud(request);
        beneficiosAsignados.setMontoAprobado(request.getBeneficioAsignado().getMontoAprobado());
        beneficiosAsignados.setFechaAsignacion(LocalDate.now());

        repositoryService.save(
                beneficiosAsignadosRepository,
                beneficiosAsignados,
                DATABASE_DEFAULT_ERROR
        );

    }

    private Integer extraerIdTipoBeneficio(String comentario){
        Pattern pattern = Pattern.compile("\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(comentario);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }
}
