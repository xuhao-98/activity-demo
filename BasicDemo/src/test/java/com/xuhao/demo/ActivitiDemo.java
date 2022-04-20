package com.xuhao.demo;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 　　@classDes:
 * 　　@author: 许昊
 * 　　@date: 2022/4/20
 *
 */
public class ActivitiDemo {
    /**
     * 部署流程定义  文件上传方式
     */
    @Test
    public void testDeployment(){
        //        1、创建ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //        2、得到RepositoryService实例
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        //        3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/leave.bpmn")//bpmn资源
                .addClasspathResource("bpmn/leave.png")
                .name("请假流程申请")
                .deploy();
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    @Test
    public void startProcess(){
        //        1、创建ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //        2、获取RunTimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //        3、根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myLeave");

        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getStartTime());
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
        //        任务负责人
        String assignee = "worker";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //        创建TaskService
        TaskService taskService = processEngine.getTaskService();
        //        根据流程key 和 任务负责人 查询任务
        List<Task> leave = taskService.createTaskQuery()
                .processDefinitionId("myLeave:1:4")
                .taskAssignee(assignee)
                .list();
        for (Task task : leave) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }
    // 完成任务
    @Test
    public void completTask(){
        //        获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //        获取taskService
        TaskService taskService = processEngine.getTaskService();
        //        根据流程key 和 任务的负责人 查询任务
        //        返回一个任务对象
        Task task = taskService.createTaskQuery()
                .taskAssignee("worker")
                .processDefinitionId("myLeave:1:4")
                .singleResult();

        //        完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * Manage查询当前个人待执行的任务
     */
    @Test
    public void testFindManageTaskList() {
        String assignee = "manager";
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("manager")
                .processDefinitionId("myLeave:1:4")
                .list();
        for (Task task : list) {
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    // 完成任务
    @Test
    public void completManagerTask(){
        //        获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //        获取taskService
        TaskService taskService = processEngine.getTaskService();
        //        根据流程key 和 任务的负责人 查询任务
        //        返回一个任务对象
        Task task = taskService.createTaskQuery()
                .taskAssignee("manager")
                .processDefinitionId("myLeave:1:4")
                .singleResult();

        //        完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * finacer查询当前个人待执行的任务
     */
    @Test
    public void testFindFinacerTaskList() {
        String assignee = "finacer";
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .processDefinitionId("myLeave:1:4")
                .list();
        for (Task task : list) {
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    // 完成任务
    @Test
    public void completFinacerTask(){
        //        获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //        获取taskService
        TaskService taskService = processEngine.getTaskService();
        //        根据流程key 和 任务的负责人 查询任务
        //        返回一个任务对象
        Task task = taskService.createTaskQuery()
                .taskAssignee("finacer")
                .processDefinitionId("myLeave:1:4")
                .singleResult();

        //        完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        //        获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        得到ProcessDefinitionQuery 对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
//          查询出当前所有的流程定义
//          条件：processDefinitionKey =evection
//          orderByProcessDefinitionVersion 按照版本排序
//        desc倒叙
//        list 返回集合
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("myLeave")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
//      输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }

    /**
     * 查询流程实例
     */
    @Test
    public void queryProcessInstance() {
        // 流程定义key
        String processDefinitionKey = "myLeave";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)//
                .list();

        for (ProcessInstance processInstance : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id："
                    + processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id："
                    + processInstance.getProcessDefinitionId());
            System.out.println("是否执行完成：" + processInstance.isEnded());
            System.out.println("是否暂停：" + processInstance.isSuspended());
            System.out.println("当前活动标识：" + processInstance.getActivityId());
            System.out.println("业务关键字："+processInstance.getBusinessKey());
        }
    }

    /**
     * 删除流程
     */
    @Test
    public void deleteDeployment() {
        // 流程部署id
        String deploymentId = "1";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
//        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Test
    public void  queryBpmnFile() throws IOException {
//        1、得到引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2、获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3、得到查询器：ProcessDefinitionQuery，设置查询条件,得到想要的流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myLeave")
                .singleResult();
//        4、通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();
//        5、通过repositoryService的方法，实现读取图片信息和bpmn信息
//        png图片的流
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
//        bpmn文件的流
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
//        6、构造OutputStream流
        File file_png = new File("d:/myLeave.png");
        File file_bpmn = new File("d:/myLeave.bpmn");
        FileOutputStream bpmnOut = new FileOutputStream(file_bpmn);
        FileOutputStream pngOut = new FileOutputStream(file_png);
//        7、输入流，输出流的转换
        IOUtils.copy(pngInput,pngOut);
        IOUtils.copy(bpmnInput,bpmnOut);
//        8、关闭流
        pngOut.close();
        bpmnOut.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo(){
//      获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
//        获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
//        查询 actinst表，条件：根据 InstanceId 查询，查询一个流程的所有历史信息
        instanceQuery.processInstanceId("2501");
//        查询 actinst表，条件：根据 DefinitionId 查询，查询一种流程的所有历史信息
//        instanceQuery.processDefinitionId("myLeave:1:22504");
//        增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
//        查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
//        输出
        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<==========================>");
        }
    }
}
