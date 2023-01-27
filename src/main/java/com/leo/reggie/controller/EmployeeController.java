package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leo.reggie.common.R;
import com.leo.reggie.entity.Employee;
import com.leo.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
        //convert password in the form of md5 encryto
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp == null){
            return R.error("Login failure!");
        }

        if(!emp.getPassword().equals(password)){
            return R.error("Login failure!");
        }

        //check the status of the employee, --1 means active, 0 means inactive
        if(emp.getStatus() == 0){
            return R.error("the account has been deactivated.");
        }

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    /**
     * logout controller
     * @param httpServletRequest
     * @return
     */

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("Exit successfully");
    }


    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){

        //set default password after encryption
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/
        employeeService.save(employee);

        return R.success("one employee added!");
    }

    /**
     * display info by page or by name
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page= {}, pageSize = {}, name = {}", page, pageSize,name);

        Page page1 = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(page1,lambdaQueryWrapper);
        return R.success(page1);

    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());
        /*Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeService.updateById(employee);
        return R.success("Update successfully!");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){

        log.info("get employee by id =="+id);
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }

        return R.error("NO such employee");
    }
}
