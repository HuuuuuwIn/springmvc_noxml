package com.zzcm.tmp.act;

import com.alibaba.fastjson.JSON;
import com.zzcm.tmp.bean.Node;
import com.zzcm.tmp.bean.Task;
import com.zzcm.tmp.service.NodeService;
import com.zzcm.tmp.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
@Controller
@RequestMapping(value = "/task")
public class TaskAct {

    private static Logger log = LoggerFactory.getLogger(TaskAct.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private NodeService nodeService;

    @RequestMapping(value="/{id}.jhtml")
    public @ResponseBody Task login(@PathVariable Integer id,ModelMap model,HttpServletRequest request,
                      HttpServletResponse response){
        log.info("id2:"+id);
        //SFTPUtil.download("192.168.0.180", "root", "zzcm2014", "/opt", "cid.jar", "D:/test/aaaa.jar");
        //ResponseUtils.renderJson(response, "true");
        return taskService.findById(1);
    }

    @RequestMapping(value="/test",produces = {"text/plain;charset=UTF-8"})
    public @ResponseBody String test(ModelMap model,HttpServletRequest request,
                                      HttpServletResponse response){
        log.info("id2:");
        //SFTPUtil.download("192.168.0.180", "root", "zzcm2014", "/opt", "cid.jar", "D:/test/aaaa.jar");
        //ResponseUtils.renderJson(response, "true");
        return "你好。";
    }

    @RequestMapping(value="/test2",produces = {"text/plain;charset=UTF-8","application/json;charset=UTF-8"})
    public @ResponseBody String test2(ModelMap model,HttpServletRequest request,
                                     HttpServletResponse response){
        log.info("id2:");
        Task task = taskService.findById(1);
        String s = JSON.toJSONString(task);
        System.out.println(s);
        return s;
    }

    @RequestMapping(value="/test3",produces = {"text/html;charset=UTF-8","application/json;charset=UTF-8"})
    public @ResponseBody String test3(ModelMap model,HttpServletRequest request,
                                      HttpServletResponse response){
        log.info("id2:");
        Task task = taskService.findById(1);
        String s = JSON.toJSONString(task);
        System.out.println(s);
        return s;
    }

    @RequestMapping(value="/")
    public String list(Model model,RedirectAttributes attr){
        List<Task> tasks = taskService.getAll();
        model.addAttribute("tasks",tasks);
        //attr.addFlashAttribute(user);
        //attr.addFlashAttribute("user",user);
        return "/task/list";
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(Model model){
        List<Node> nodes =nodeService.getAllNodes();
        model.addAttribute("nodes",nodes);
        return "/task/add";
    }

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public String add(@ModelAttribute Task task, Model model){
        Node node = nodeService.findById(task.getFrom().getId());
        task.setFrom(node);
        taskService.insertTask(task);
        return "redirect:/task/";
    }

    @RequestMapping(value="/update/{id:\\d+}",method = RequestMethod.GET)
    public String update(@PathVariable Integer id,Model model){
        List<Node> nodes =nodeService.getAllNodes();
        Task task = taskService.findById(id);
        if(task.getStatus() != Task.Status.STOP){
            return "redirect:/task/";
        }
        model.addAttribute("task",task);
        model.addAttribute("nodes",nodes);
        return "/task/add";
    }

    @RequestMapping(value="/update",method = RequestMethod.POST)
    public String update(@ModelAttribute Task task, Model model){
        //Node node = nodeService.findById(task.getFrom().getId());
        //task.setFrom(node);
        taskService.saveOrUpdate(task);
        return "redirect:/task/";
    }

    @RequestMapping(value="/delete/{id}")
    public String delete(@PathVariable Integer id, Model model){
        taskService.deleteTask(id);
        return "redirect:/task/";
    }

    @RequestMapping(value="/pause/{id}")
    public String pause(@PathVariable Integer id, Model model){
        taskService.pauseTask(id);
        return "redirect:/task/";
    }

    @RequestMapping(value="/resume/{id}")
    public String resume(@PathVariable Integer id, Model model){
        taskService.resumeTask(id);
        return "redirect:/task/";
    }

    @RequestMapping(value="/start/{id}")
    public String start(@PathVariable Integer id, Model model){
        taskService.startTask(id);
        return "redirect:/task/";
    }

    @RequestMapping(value="/stop/{id}")
    public String stop(@PathVariable Integer id, Model model){
        taskService.stopTask(id);
        return "redirect:/task/";
    }

    @RequestMapping(value="/enable/{id}")
    public String enable(@PathVariable Integer id, Model model){
        Task task = taskService.findById(id);
        if(null!=task && task.getStatus()== Task.Status.STOP){
            task.setEnable(true);
            taskService.update(task);
        }
        return "redirect:/task/";
    }

    @RequestMapping(value="/disable/{id}")
    public String disable(@PathVariable Integer id, Model model){
        Task task = taskService.findById(id);
        if(null!=task && task.getStatus()== Task.Status.STOP){
            task.setEnable(false);
            taskService.update(task);
        }
        return "redirect:/task/";
    }
}
