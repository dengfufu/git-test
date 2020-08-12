package com.zjft.usp.common.resolver;

import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.constant.SecurityConstants;
import com.zjft.usp.common.feign.service.UserFeignService;
import com.zjft.usp.common.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


/**
 * Token转化为User
 *
 * @author CK
 * @date 2019-08-06 22:20
 */
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    private UserFeignService userFeignService;

    public TokenArgumentResolver(UserFeignService userFeignService) {
        this.userFeignService = userFeignService;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        boolean isFull = loginUser.isFull();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String userId = request.getHeader(SecurityConstants.USER_ID_HEADER);
        String roles = request.getHeader(SecurityConstants.ROLE_HEADER);
        if (StrUtil.isBlank(userId) || userId.equals("null")) {
            log.warn("resolveArgument error userId is empty");
            return null;
        }

        UserInfo userInfo;
        if (isFull) {
            userInfo = userFeignService.selectByUserId(userId);
        } else {
            userInfo = new UserInfo();
            userInfo.setUserId(Long.parseLong(userId));
        }
//        List<Role> roleList = new ArrayList<>();
////        Arrays.stream(roles.split(",")).forEach(roleItem -> {
////            Role role = new Role();
////            role.setCode(roleItem);
////            roleList.add(role);
////        });
//        userInfo.setRoles(roleList);
        return userInfo;
    }
}
