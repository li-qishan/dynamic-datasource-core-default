package com.johns.dynamicdatasource.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.johns.dynamicdatasource.common.api.R;
import com.johns.dynamicdatasource.service.MemberService;
import com.johns.dynamicdatasource.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;

    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public R generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return R.success(sb.toString(), "获取验证码成功");
    }


    //对输入的验证码进行校验
    @Override
    public R verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return R.failed("请输入验证码");
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return R.success(null, "验证码校验成功");
        } else {
            return R.failed("验证码不正确");
        }
    }

}
