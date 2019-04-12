// package com.lzsz.multidatasource.jdbc.scan.common;
//
// import org.apache.commons.lang3.StringUtils;
//
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
//
// /**
//  * @author wanghaibo
//  * @version V1.0
//  * @desc 选择设置查询数据源的工具
//  * @date 2019/4/4 16:45
//  */
// public class ChoiceHelperbak {
//     private static final ThreadLocal<String> LOCAL_CHOICE = new ThreadLocal<>();
//     private static final Map<String, String> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
//     private static final String CONCURRENT_HASH_MAP_KEY = "CONCURRENT_HASH_MAP_KEY";
//
//     // TODO: 2019/4/12
//     // 设置值 可以单个线程独享 注意：不要在设置完这个后使用多线程调用
//     // 设置固定的值 可以多个线程共享 注意：不想在使用完这个后使用别的线程继续调用 应该手工clear掉
//
//     public static void choice(String name) {
//         final String name1 = LOCAL_CHOICE.getClass().getName();
//
//         if (StringUtils.isBlank(name)) {
//             return;
//         }
//         clear();
//         LOCAL_CHOICE.set(name);
//     }
//
//     public static void choice(String name, Boolean fixed) {
//         if (StringUtils.isBlank(name)) {
//             return;
//         }
//         clear();
//         if (fixed == null || !fixed) {
//             LOCAL_CHOICE.set(name);
//         } else {
//             CONCURRENT_HASH_MAP.put(CONCURRENT_HASH_MAP_KEY, name);
//         }
//     }
//
//     public static String chooser(boolean fixed) {
//         if (fixed) {
//             return CONCURRENT_HASH_MAP.get(CONCURRENT_HASH_MAP_KEY);
//         }
//         return LOCAL_CHOICE.get();
//     }
//
//     /**
//      * 移除本地变量
//      */
//     public static void clear() {
//         CONCURRENT_HASH_MAP.put(CONCURRENT_HASH_MAP_KEY, "");
//         LOCAL_CHOICE.remove();
//     }
//
// }
