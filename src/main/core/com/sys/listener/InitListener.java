package com.sys.listener;


import com.sys.util.CacheUtil;

import javax.servlet.ServletContextEvent;

/**
 * Created by lenovo on 2015/1/27.
 */
public class InitListener implements javax.servlet.ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        CacheUtil.clearAllCache();
    }
}
