package com.mepeng.cn.SevenPen.account.service;

import org.dromara.hmily.annotation.Hmily;
import com.mepeng.cn.SevenPen.account.dto.AccountDTO;
import com.mepeng.cn.SevenPen.account.entity.AccountDO;

/**
 * AccountService.
 *
 * @author xiaoyu
 */
public interface AccountService {

    /**
     * 扣款支付.
     *
     * @param accountDTO 参数dto
     * @return true
     */
    @Hmily
    boolean payment(AccountDTO accountDTO);

    /**
     * 获取用户账户信息.
     *
     * @param userId 用户id
     * @return AccountDO
     */
    AccountDO findByUserId(String userId);
}
