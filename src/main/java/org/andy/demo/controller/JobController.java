package org.andy.demo.controller;

import org.andy.toolkit.quartz.QuartzUtils;
import org.andy.demo.entity.JobAndTrigger;
import org.andy.demo.service.IJobAndTriggerService;
import com.github.pagehelper.PageInfo;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 当前Controller Bean 实现接口 InitializingBean 用于在Bean实例化后马上执行一段初始化代码 , 见 afterPropertiesSet() 内逻辑
 */
@RestController
@RequestMapping(value = "/job")
public class JobController implements InitializingBean {
    @Autowired
    IJobAndTriggerService iJobAndTriggerService;

    @Autowired
    Scheduler scheduler;

    private static Logger log = LoggerFactory.getLogger(JobController.class);


    @PostMapping(value = "/add-job")
    public void addJob(@RequestParam(value = "jobClassName") String jobClassName,
                       @RequestParam(value = "jobGroupName") String jobGroupName,
                       @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        QuartzUtils.addJob(jobClassName, jobGroupName, cronExpression);
    }

    @PostMapping(value = "/pause-job")
    public void pauseJob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        QuartzUtils.jobPause(jobClassName, jobGroupName);
    }

    @PostMapping(value = "/resume-job")
    public void resumejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        QuartzUtils.jobResume(jobClassName, jobGroupName);
    }


    @PostMapping(value = "/reschedule-job")
    public void rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
                              @RequestParam(value = "jobGroupName") String jobGroupName,
                              @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        QuartzUtils.jobReschedule(jobClassName, jobGroupName, cronExpression);
    }


    @PostMapping(value = "/delete-job")
    public void deleteJob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        QuartzUtils.jobDelete(jobClassName, jobGroupName);
    }

    @GetMapping(value = "/query-job")
    public Map<String, Object> queryJob(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        PageInfo<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        return map;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // 2017-07-27
        // 如果想使用 Spring boot 配置类 SchedulerConfig 中配置的 Scheduler 实例，不要注释下面一行;
        // 如果想使用 Quartz 缺省的 StdScheduler 实例，注释掉下面一行
        QuartzUtils.setScheduler(scheduler);
    }
}
