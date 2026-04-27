package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.exception.CustomException;
import com.example.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

//通过@Service把其标注为springboot的一个bean
@Service
public class EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    public void add(Employee employee) {
        String username = employee.getUsername(); //账号
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee != null){//注册账号已存在 无法注册
            throw new CustomException("500","账号已存在,请更换其他账号");
        }
        Employee dbEmployee1 = employeeMapper.selectByNo(employee.getNo());
        if(dbEmployee1 != null){//工号唯一
            throw new CustomException("500","工号已存在,请确认是否正确");
        }
        if(StrUtil.isBlank(employee.getPassword())){//密码没填
            employee.setPassword("123");//默认密码123
        }
        if(StrUtil.isBlank(employee.getName())){//密码没填
            employee.setName(employee.getUsername());//默认名称
        }
        //一定要设置角色
        employee.setRole("EMP"); //员工的角色
        employeeMapper.insert(employee);

    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    /*
    可用Mybatis-Plus的removeByIds(推荐)
    或者让employeeMapper继承BaseMapper 里面提供了CRUD功能//employeeMapper.deleteBatchIds(ids);
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);//多次调用性能差耗时长
        }
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }


    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }


    public Account login(Account account) {
        String username = account.getUsername(); //账号
        Employee dbEmployee = employeeMapper.selectByUsername(username);//通过创建一个selectByUsername方法来查询该用户名是否存在
        if(dbEmployee == null){//未查询到该用户
            throw new CustomException("500","该账号不存在");//抛出错误 自定义异常
        }
        //数据库存在该账号
        String password = account.getPassword();//数据库密码
        //dbEmployee.getPassword()为输入的密码
        if(!dbEmployee.getPassword().equals(password)){//用户输入的密码与数据库的密码不匹配
            throw new CustomException("500","账号或密码错误");
        }
        return dbEmployee;
    }

    public void register(Employee employee) {
        this.add(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee = this.selectById(id);
        if(!employee.getPassword().equals(account.getPassword())){ //页面传来的密码与数据库的密码不匹配,不匹配就报错
            throw new CustomException("500","对不起原密码错误");
        }
        employee.setPassword(account.getNewPassword());//设置新密码
        this.update(employee);
    }
}
