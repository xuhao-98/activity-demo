package com.xuhao.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * @author ：xuhao
 * @date ：Created in 2021/4/20
 * @description:
 **/

public class TestCreateTable {
    /**
     * 生成 activiti的数据库表
     */
    @Test
    public void testCreateDbTable() {
        //默认创建方式
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通用的创建方式，指定配置文件名和Bean名称
//        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
//        ProcessEngine processEngine1 = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);

    }
}
