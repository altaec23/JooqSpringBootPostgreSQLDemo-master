package com.altaec.jooq.recurse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JooqDemoRecurseApplicationTests {

    @Autowired
    private DirectoryService directoryService;

    @Test
    void contextLoads() {
        directoryService.getResultWithLimitData();
    }

}
