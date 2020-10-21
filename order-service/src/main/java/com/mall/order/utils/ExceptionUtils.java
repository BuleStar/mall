package com.mall.order.utils;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author hongfei
 */
public class ExceptionUtils {

    public static String exHandler(BlockException e){

        return "exHandler error";
    }
}
