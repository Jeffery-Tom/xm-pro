package com.example.mapper;

import com.example.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepartmentMapper {
    List<Department> selectAll(Department department);

    //简单语句可以直接在mapper里用注解写,遇到比较复杂的建议在xml里写语句
    @Select("select * from `department` where id = #{id}")
    Department selectById(Integer id);

    void insert(Department department);

    void updateById(Department department);

    @Delete("delete from `department` where id = #{id}")
    void deleteById(Integer id);
}
