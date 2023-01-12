package com.rocky.bistro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocky.bistro.common.R;
import com.rocky.bistro.entity.Employee;
import com.rocky.bistro.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 管理系统登录功能
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp == null)
            return R.error("用户不存在");

        if(!emp.getPassword().equals(password))
            return R.error("密码错误");

        if(emp.getStatus() == 0)
            return R.error("该员工已被禁用");

        //登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出登陆成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工信息：{}",employee.toString());
        //设置初始化密码，进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间和更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //获取session中的用户id，设置创建人id和更新人id
//        Long eID = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(eID);
//        employee.setUpdateUser(eID);
        //调用服务
        employeeService.save(employee);
        return R.success("新增用户成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
        //构造分页查询器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R change(HttpServletRequest request, @RequestBody Employee employee){
        log.info("修改员工状态,"+employee.toString());

//        Long eID = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(eID);
        employeeService.updateById(employee);
        return  R.success("修改完成");
    }

    @GetMapping("/{id}")
    public R<Employee> queryById(@PathVariable Long id){
        log.info("查询员工"+ id +"信息");
        Employee employee = employeeService.getById(id);
        if(employee != null)
            return R.success(employee);
        return R.error("未查询到该员工");
    }
}
