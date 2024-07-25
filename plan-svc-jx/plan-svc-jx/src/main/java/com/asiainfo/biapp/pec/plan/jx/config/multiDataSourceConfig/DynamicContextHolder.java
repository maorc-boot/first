package com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig
 * @className: DynamicContextHolder
 * @author: chenlin16
 * @description: TODO
 * @date: 2023/7/12 18:31
 */
public class DynamicContextHolder {
    @SuppressWarnings("unchecked")
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new ArrayDeque();
        }
    };

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     *
     * @param dataSource 数据源名称
     */
    public static void push(String dataSource) {
        CONTEXT_HOLDER.get().push(dataSource);
    }

    /**
     * 清空当前线程数据源
     */
    public static void poll() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }

}
