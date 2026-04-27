package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

//负责接收前端传过来的数据
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;
    /*
    get:查询操作
    post:新增操作
    put:修改操作
    delete:删除操作
     */

    /*
    新增数据
     */
    @PostMapping("/add")//@RequestBody把前端传来的JSON字符串映射出JAVA对象,或者数组
    public Result add(@RequestBody Employee employee){
        employeeService.add(employee);
        return Result.success();//新增数据无需返回数据
    }

    /*
    更新数据
     */
    @PutMapping("/update")//@RequestBody把前端传来的JSON字符串映射出JAVA对象,或者数组
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success();//新增数据无需返回数据
    }

    /*
    删除单个数据
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        employeeService.deleteById(id);
        return Result.success();//新增数据无需返回数据
    }

    /*
    批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//需要用@RequestBody接收因为是个数组
        employeeService.deleteBatch(ids);
        return Result.success();//新增数据无需返回数据
    }

    /*
    查询单个数据
 */
    @GetMapping("/selectById/{id}")//路径传参
    public Result selectById(@PathVariable Integer id){
        Employee employee= employeeService.selectById(id);
        return Result.success(employee);
    }
    
    //查询所有数据 同时也是定义一个接口
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list= employeeService.selectAll(employee);
    return Result.success(list);
    }

    /*
    分页数据的查询
    pageNum: 当前页码
    pageSize: 每页的个数
 */
    @GetMapping("/selectPage")//路径传参
    public Result selectPage(Employee employee,//这些参数是从前端传过来的
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize){
        //service mapper sql都走过一遍之后传回来pageInfo给前端
        PageInfo<Employee> pageInfo= employeeService.selectPage(employee,pageNum,pageSize);//PageInfo是什么?
        return Result.success(pageInfo);
    }


    /*
    导出数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        //1.拿到所有员工的数据
        List<Employee> employeeList = employeeService.selectAll(null);
        //2.构建ExcelWriter
        //在内存操作,写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3设置中文表头
        writer.addHeaderAlias("username","账号");
        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("no","工号");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("description","个人介绍");
        writer.addHeaderAlias("departmentName","部门");
        //默认的,未添加alias的属性也会写出,如果只想写出加了别名的字段,可以调用此方法排除
        writer.setOnlyAlias(true);
        //4. 写出数据到writer
        writer.write(employeeList,true);
        // 5.设置输出文件名称  以及输出流的头信息
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");//固定格式
        String fileName = URLEncoder.encode("员工信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //6.写出到输出流
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception{
        //1. 拿到输入流 构建reader
        InputStream inputstream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputstream);
        //2.读取 excel里的数据
        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("性别","sex");
        reader.addHeaderAlias("工号","no");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("个人介绍","description");
        reader.addHeaderAlias("部门","departmentName");
        List<Employee> employeeList = reader.readAll(Employee.class);
        //3.写入list数据到数据库
        for (Employee employee : employeeList) {
            employeeService.add(employee);
        }
        return Result.success();
    }
}
