package com.evgeniy.spring.springapplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestMailConfig.class)
@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}
