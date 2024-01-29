package com.flix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
@SpringBootTest(properties = {"aws.s3.region=us-east-1", "propB=valueB"})
@TestPropertySource(locations="classpath:application-test.properties")
class FlixApplicationTests {

    @Test
    void contextLoads() {
    }

}
