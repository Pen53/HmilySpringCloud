package com.mepeng.cn.SevenPen.account.service.impl;

import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import com.mepeng.cn.SevenPen.account.dto.AccountDTO;
import com.mepeng.cn.SevenPen.account.entity.AccountDO;
import com.mepeng.cn.SevenPen.account.mapper.AccountMapper;
import com.mepeng.cn.SevenPen.account.service.AccountService;
import com.mepeng.cn.SevenPen.account.service.InLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Account service.
 *
 * @author xiaoyu
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountMapper accountMapper;

    @Autowired
    private InLineService inLineService;

    /**
     * Instantiates a new Account service.
     *
     * @param accountMapper the account mapper
     */
    @Autowired(required = false)
    public AccountServiceImpl(final AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean payment(final AccountDTO accountDTO) {
        LOGGER.debug("============执行try付款接口===============");
        accountMapper.update(accountDTO);
        //内嵌调用
        //inLineService.test();
        return Boolean.TRUE;
    }

    @Override
    public AccountDO findByUserId(final String userId) {
        return accountMapper.findByUserId(userId);
    }

    /**
     * Confirm boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    public boolean confirm(final AccountDTO accountDTO) {
        LOGGER.debug("============执行confirm 付款接口===============");
        final int rows = accountMapper.confirm(accountDTO);
        return Boolean.TRUE;
    }


    /**
     * Cancel boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    public boolean cancel(final AccountDTO accountDTO) {
        LOGGER.debug("============执行cancel 付款接口===============");
        final int rows = accountMapper.cancel(accountDTO);
        if (rows != 1) {
            throw new HmilyRuntimeException("取消扣减账户异常！");
        }
        return Boolean.TRUE;
    }
}
