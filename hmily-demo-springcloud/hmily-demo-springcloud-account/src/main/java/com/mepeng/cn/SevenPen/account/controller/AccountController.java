package com.mepeng.cn.SevenPen.account.controller;

import com.mepeng.cn.SevenPen.account.dto.AccountDTO;
import com.mepeng.cn.SevenPen.account.service.AccountService;
import com.mepeng.cn.SevenPen.account.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * AccountController.
 * @author xiaoyu
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/payment")
    public Boolean save(@RequestBody AccountDTO accountDO) {
        return accountService.payment(accountDO);
    }

    @RequestMapping("/findByUserId")
    public BigDecimal findByUserId(@RequestParam("userId") String userId) {
        return accountService.findByUserId(userId).getBalance();
    }


    @Autowired
    private DemoService demoService;
    @RequestMapping("/testTcc")
    public Boolean testTcc(@RequestBody Map<String,Object> params) {
        System.out.println("AccountController testTcc is doing params:"+params);
        return demoService.testTcc(params);
    }

    @RequestMapping("/testTCCFeginAccountTryException")
    public Boolean testTCCFeginAccountTryException(@RequestBody Map<String,Object> params) {
        System.out.println("AccountController testTCCFeginAccountTryException is doing params:"+params);
        return demoService.testTcc(params);
    }

}
