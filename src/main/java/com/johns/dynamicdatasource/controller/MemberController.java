package com.johns.dynamicdatasource.controller;

import com.johns.dynamicdatasource.common.api.R;
import com.johns.dynamicdatasource.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员登录注册管理Controller
 * Created by macro on 2018/8/3.
 */
@Controller
@Api(tags = "多租户管理端--验证码模块", description = "会员登录注册管理")
@RequestMapping("/sso")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public R getAuthCode(@RequestParam String telephone) {
        return memberService.generateAuthCode(telephone);
    }

    @ApiOperation("判断验证码是否正确")
    @RequestMapping(value = "/verifyAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public R updatePassword(@RequestParam String telephone,
                                       @RequestParam String authCode) {
        return memberService.verifyAuthCode(telephone,authCode);
    }
}
