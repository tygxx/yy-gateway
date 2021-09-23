package com.yy.ds.gateway;

import com.yy.ds.gateway.feign.SystemFeignClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeignTest {

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Test
    void systemFeign() {
        System.out.println(systemFeignClient.getUserById(1L).toString());
    }

}