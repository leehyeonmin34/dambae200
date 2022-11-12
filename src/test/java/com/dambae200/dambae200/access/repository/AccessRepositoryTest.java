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
        int a = 4;
        double b = 0.3;
        double c = 0;
        c = a / b;
        System.out.println(c);

        double a2 = 1.4;
        double b2 = 3.0;
        double c2 = 0;
        c2 = a2 / b2;
        System.out.println(c2);

        double a3 = 1.4;
        int b3 = 3;
        double c3 = 0;
        c3 = a3 / b3;
        System.out.println(c3);

    }

}
