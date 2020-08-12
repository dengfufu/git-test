package com.zjft.usp.wechat;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxMpServiceTest {
    @Autowired
    private WxMpService wxMpService;


    @Test
    public void checkStorageTest() throws WxErrorException {
        wxMpService.getAccessToken();
    }
}
