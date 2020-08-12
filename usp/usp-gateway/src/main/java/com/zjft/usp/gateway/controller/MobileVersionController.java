package com.zjft.usp.gateway.controller;

import com.zjft.usp.common.model.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * APP版本检查
 *
 * @author: CK
 * @create: 2020-01-09 16:16
 */
@RefreshScope
@RestController
public class MobileVersionController {

    @Value("${mobile.version}")
    String version;

    @Value("${mobile.apk-url}")
    String apkUrl;

    @Value("${mobile.app-url}")
    String appUrl;

    @Value("${mobile.release-note}")
    String releaseNote;

    @GetMapping(value = "/api/app/version/check4update")
    public Mono<Result> checkForUpdate() {
        return Mono.just(Result.succeed(new HashMap<String, String>() {
            {
                put("version", version);
                put("apkUrl", apkUrl);
                put("appUrl", appUrl);
                put("releaseNote", releaseNote);
            }
        }));
    }
}
