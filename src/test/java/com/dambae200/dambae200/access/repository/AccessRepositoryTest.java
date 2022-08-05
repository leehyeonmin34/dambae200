package com.dambae200.dambae200.access.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.mockito.Mockito.lenient;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class AccessRepositoryTest {

    @Test
    @DisplayName("직원 accessId로 관리자 access 엔티티 가져오기 Success")
    void FindAdminUserAccessByStaffAccessIdSuccess(){
        // WHEN
//        lenient().

    }

}
