package com.yy.ds.gateway;

import com.yy.ds.gateway.feign.SystemService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeignTest {

    @Autowired
    private SystemService systemService;

    @Test
    void systemFeign() {
        System.out.println(systemService.getUserById(1L).toString());
    }

}