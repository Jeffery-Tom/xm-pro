package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//通过@Service把其标注为springboot的一个bean
@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        String username = admin.getUsername(); //账号
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin != null){//注册账号已存在 无法注册
            throw new CustomException("500","账号已存在,请更换其他账号");
        }
        if(StrUtil.isBlank(admin.getPassword())){//密码没填
            admin.setPassword("admin");//默认密码
        }
        if(StrUtil.isBlank(admin.getName())){
            admin.setName(admin.getUsername());//默认名称
        }
        //一定要设置角色
        admin.setRole("ADMIN"); //管理员的角色
        adminMapper.insert(admin);

    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }


    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }


    public Admin login(Account account) {
        String username = account.getUsername(); //账号
        Admin dbAdmin = adminMapper.selectByUsername(username);//通过创建一个selectByUsername方法来查询该用户名是否存在
        if(dbAdmin == null){//未查询到该用户
            throw new CustomException("500","该账号不存在");//抛出错误 自定义异常
        }
        //数据库存在该账号
        String password = account.getPassword();//数据库密码
        //dbAdmin.getPassword()为输入的密码
        if(!dbAdmin.getPassword().equals(password)){//用户输入的密码与数据库的密码不匹配
            throw new CustomException("500","账号或密码错误");
        }
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if(!admin.getPassword().equals(account.getPassword())){ //页面传来的密码与数据库的密码不匹配,不匹配就报错
            throw new CustomException("500","对不起原密码错误");
        }
        admin.setPassword(account.getNewPassword() );//设置新密码
        this.update(admin);
    }
}
