// package com.backend.portalroshkabackend.Controllers.Operations;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.web.PageableDefault;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
// import com.backend.portalroshkabackend.Services.Operations.IUsuarioisService;

// @RestController("usuarioisController")
// @RequestMapping("/api/v1/admin/operations/usuariois")
// public class UsuarioisEquipoController {
//     private final IUsuarioisService usuarioisService;

//     @Autowired
//     public UsuarioisEquipoController(IUsuarioisService usuarioisService) {
//         this.usuarioisService = usuarioisService;
//     }

//     @GetMapping("")
//     public ResponseEntity<Page<UsuarioisResponseDto>> getAllUsuariosEquipo(
//             @PathVariable Integer idEquipo;
//             @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable) {
//         Page<UsuarioisResponseDto> rols = usuarioisService.getAllUsuariosEquipo(Integer idEquipo, Pageable pageable);return ResponseEntity.ok(rols);
// }

// // @PutMapping("/{id}")
// // public ResponseEntity<Page<UsuarioisResponseDto>> putEquipoUsuario(
// // return ResponseEntity.ok(rols);
// // }
// }
