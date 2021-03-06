package com.github.gkislin.common.config;

import com.github.gkislin.common.LoggerWrapper;
import com.github.gkislin.common.util.JmxUtil;

import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: gkislin
 * Date: 25.02.14
 */
public class ConfigListener implements ServletContextListener {
    protected final LoggerWrapper logger = LoggerWrapper.get(getClass());

    protected static IConfig CONFIG;
    protected ObjectName beanObjectName = null;

/*
    protected static AbstractConfig CONFIG;

    public static AbstractConfig getConfig() {
        return CONFIG;
    }
*/

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String configClassName = sce.getServletContext().getInitParameter("config");
        if (configClassName == null) {
            throw logger.getIllegalArgumentException("context-param 'config' not found");
        }
        try {
            CONFIG = (AbstractConfig) Class.forName(configClassName).getMethod("get").invoke(null);
            beanObjectName = JmxUtil.registerMBean(CONFIG, "type=Config");

            logger.info("\n+++++++++++++++++++++ Application Initialized +++++++++++++++++++++++++++++\n" + CONFIG.toString() +
                    "\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

        } catch (Throwable e) {
            throw logger.getIllegalArgumentException("Configuration class " + configClassName + " couldn't be created", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("\n+++++++++++++++++++++ Application Destroyed +++++++++++++++++++++++++++++\n");
    }

    public static IConfig getConfig() {
        return CONFIG;
    }
}
