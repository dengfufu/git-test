package com.zjft.usp.auth;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.auth.business.model.Token;
import com.zjft.usp.auth.business.service.TokensService;
import com.zjft.usp.redis.template.RedisRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UspAuthApplicationTests {

    @Autowired
    TokensService tokensService;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test1() {
        System.out.println(redisRepository.getRedisTemplate());
    }

    @Test
    public void contextLoads() {
        Page page = new Page();
        page.setCurrent(0);
        page.setSize(1);
        IPage<Token> list = this.tokensService.listTokens(page, null, "10001");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void test() {
        System.out.println(redisRepository.getRedisTemplate().opsForSet().members("client_id_to_access:10001"));
        System.out.println(redisRepository.getRedisTemplate().opsForList().size("client_id_to_access:10001"));
    }

    @Test
    public void test2() {
        System.out.println(passwordEncoder.encode("123456"));
    }

}
