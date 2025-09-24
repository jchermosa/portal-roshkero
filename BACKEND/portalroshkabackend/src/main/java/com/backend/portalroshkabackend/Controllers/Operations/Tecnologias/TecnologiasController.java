// package com.backend.portalroshkabackend.Controllers.Operations.Tecnologias;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.web.PageableDefault;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;
// import com.backend.portalroshkabackend.Services.Operations.Interface.Tecnologias.ITecnologiasService;

// @RestController("tecnologiasController")
// @RequestMapping("/api/v1/admin/operations")
// public class TecnologiasController {
//     private final ITecnologiasService tecnologiasService;

//     @Autowired
//     public TecnologiasController(ITecnologiasService tecnologiasService) {
//         this.tecnologiasService = tecnologiasService;
//     }

//     @GetMapping("/tecnologias")
//     public ResponseEntity<Map<String, Object>> getAllTecnologias(
//             @PageableDefault(size = 10, sort = "idTecnologia", direction = Sort.Direction.ASC) Pageable pageable,
//             @RequestParam(required = false, defaultValue = "default") String sortBy) {

//         Page<TecnologiasResponseDto> page = tecnologiasService.getAllTecnologias(pageable, sortBy);

//         // Better json for front
//         Map<String, Object> response = new HashMap<>();
//         response.put("content", page.getContent());
//         response.put("currentPage", page.getNumber());
//         response.put("totalItems", page.getTotalElements());
//         response.put("totalPages", page.getTotalPages());

//         return ResponseEntity.ok(response);
//     }

// }
