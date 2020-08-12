package com.zjft.usp.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.auth.business.mapper.ClientMapper;
import com.zjft.usp.auth.business.model.Client;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


/**
 * @author CK
 * @date 2019-09-19 16:28
 */
// @RunWith(SpringRunner.class)
// @SpringBootTest
public class MybatisTest {

    @Resource
    private ClientMapper clientMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    // @Test
    public void test() {
        System.out.println(sqlSessionFactory.openSession().getConnection());
        System.out.println(clientMapper);
        Page page = new Page();
        page.setCurrent(1);
        page.setSize(1);
        IPage<Client> list = clientMapper.selectPage(page, null);
        System.out.println(JSON.toJSONString(list));
    }
}
