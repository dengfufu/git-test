package com.zjft.usp.uas;

import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UspUasApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() {

        System.out.println(this.passwordEncoder.encode("123456"));
        System.out.println(this.passwordEncoder.encode("123456"));
        System.out.println(this.passwordEncoder.encode("123456"));
        System.out.println(this.passwordEncoder.encode("123456-QWE_asdfg"));
        System.out.println(this.passwordEncoder.encode("123456-QWE_asdfg").length());
        System.out.println(this.passwordEncoder.encode("qwerfdsa123456789"));
        System.out.println(this.passwordEncoder.encode("Qwerfdsa123456789"));


        System.out.println(new Md5Crypt());
        System.out.println(this.passwordEncoder.encode("123456"));


        System.out.println(this.passwordEncoder.encode("usp"));
    }

}
