package com.backend.portalroshkabackend.Services;

import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.Services.HumanResource.IThSelfService;
import com.backend.portalroshkabackend.tools.errors.errorslist.DatabaseOperationException;
import com.backend.portalroshkabackend.tools.validator.EmployeeValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class HumanResourceServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeValidator employeeValidator;

    @InjectMocks
    private IThSelfService service;

    HumanResourceServiceImplTest(){
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testUpdateEmail_ThrowsDatabaseOperationException() {

    //     Usuario fakeUser = new Usuario();
    //     fakeUser.setIdUsuario(1);

        // when(userRepository.findById(1)).thenReturn(Optional.of(fakeUser));
        // doNothing().when(employeeValidator).validateUniqueEmail(any(), any());
        // when(userRepository.save(any(Usuario.class)))
        //         .thenThrow(new JpaSystemException(new RuntimeException("DB error")));

    //     assertThrows(DatabaseOperationException.class, () -> {
    //         service.updateEmail(1, "test@test.com");
    //     });
    // }

}
