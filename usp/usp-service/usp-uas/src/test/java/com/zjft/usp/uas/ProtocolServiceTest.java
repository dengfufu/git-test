package com.zjft.usp.uas;

import com.alibaba.fastjson.JSON;
import com.zjft.usp.uas.protocol.dto.ProtocolCheckDto;
import com.zjft.usp.uas.protocol.service.ProtocolSignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author: CK
 * @create: 2020-06-18 14:37
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProtocolServiceTest {

    @Autowired
    ProtocolSignService protocolSignService;

    @Test
    public void signToBTest() {
        protocolSignService.signToB(1L, 1L, Arrays.asList(1, 2, 3));
    }

    @Test
    public void signToCTest() {
        protocolSignService.signToC(1L, Arrays.asList(1, 2, 3));
    }

    @Test
    public void checkSignTest() {
        List<ProtocolCheckDto> list = protocolSignService.checkSign(1L, null, "");
        System.out.println(JSON.toJSONString(list));
    }


}
