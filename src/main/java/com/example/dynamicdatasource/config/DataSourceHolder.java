package com.example.dynamicdatasource.config;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class DataSourceHolder {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceHolder.class);

    //线程本地环境
    private static final ThreadLocal<String> contextHolders = new ThreadLocal<String>();
    //数据源列表
    public static List<String> dataSourceIds = Lists.newArrayList();
    //设置数据源
    public static void setDataSource(String customerType) {
        contextHolders.set(customerType);
    }
    //获取数据源
    public static String getDataSource() {
        return (String) contextHolders.get();
    }
    //清除数据源
    public static void clearDataSource() {
        contextHolders.remove();
    }

    public static void master() {
        logger.info("Switch to the master database");
        contextHolders.set(DynamicDatasourceConfig.MASTER);
    }

    public static void slave() {
        int index = new Random().nextInt(dataSourceIds.size());
        String db = dataSourceIds.get(index);
        logger.info("Switch database to: {}", db);
        contextHolders.set(db);
    }


    /**
     * 判断指定DataSrouce当前是否存在
     */
    public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }
}
