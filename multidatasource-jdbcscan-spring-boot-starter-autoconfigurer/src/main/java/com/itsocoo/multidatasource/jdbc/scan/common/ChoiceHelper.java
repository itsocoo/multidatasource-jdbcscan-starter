package com.itsocoo.multidatasource.jdbc.scan.common;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 选择设置查询数据源的工具
 * @date 2019/4/4 16:45
 */
public class ChoiceHelper {
    private static final ThreadLocal<String> LOCAL_CHOICE = new ThreadLocal<>();

    public static void choice(String name) {
        clear();
        LOCAL_CHOICE.set(name);
    }


    public static String choice() {
        return LOCAL_CHOICE.get();
    }

    /**
     * 移除本地变量
     */
    public static void clear() {
        LOCAL_CHOICE.remove();
    }

}
