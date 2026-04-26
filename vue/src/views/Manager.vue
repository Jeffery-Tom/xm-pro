<template>
  <div>
    <!--  头部开始-->
    <div style="height: 60px;background-color: #247aea; display: flex;align-items: center ">
      <div style="width: 200px; display: flex;align-items: center;padding-left: 15px" >
        <img style="width: 35px;" src="@/assets/sun.png" alt="">
        <span style="color: #ffffff;font-size: 20px;margin-left: 5px">后台管理系统</span>
      </div>
      <div style="flex: 1"></div>
      <div style="width: fit-content;display: flex;align-items: center;padding-right: 20px">
        <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" style="width: 40px;height: 40px;border-radius: 50%" alt="" bor>
        <span style="color: white;margin-left: 10px">{{ data.user.name }}</span>
      </div>
    </div>
    <!--  头部结束-->

    <!--  下面区域开始-->
    <div style="display: flex">
      <!--  左侧导航开始-->
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px) ">
        <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1']" style="border: 0">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            系统首页
          </el-menu-item>
          <el-menu-item index="/manager/data">
            <el-icon><DataAnalysis /></el-icon>
            数据统计
          </el-menu-item>
          <el-menu-item index="/manager/article" v-if="data.user.role === 'ADMIN'">
            <el-icon><Document /></el-icon>
            文章管理
          </el-menu-item>
          <el-menu-item index="/manager/department" v-if="data.user.role === 'ADMIN'">
            <el-icon><OfficeBuilding /></el-icon>
            部门管理
          </el-menu-item>
          <el-sub-menu index="1" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/employee">员工信息</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manager/person">
            <el-icon><UserFilled/></el-icon>
            个人信息
          </el-menu-item>
          <el-menu-item index="/manager/password">
            <el-icon><Lock/></el-icon>
            修改密码
          </el-menu-item>
          <el-menu-item @click="logout">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-menu-item>

        </el-menu>
      </div>
      <!--  左侧导航结束-->

      <!--  右侧主体开始-->
      <div style="flex: 1;width: 0;background-color: #efeff4;padding: 10px">
<!--@updateUser触发父组件 从updateUser方法里拿到子组件传过来的最新数据JSON.parse(localStorage.getItem('xm-pro-user')-->
        <RouterView @updateUser="updateUser"/>
      </div>
      <!--  右侧主体结束-->
    </div>
    <!--  下面区域结束-->
  </div>


</template>

<script setup>

import {
  DataAnalysis,
  Document,
  House,
  Lock,
  OfficeBuilding,
  SwitchButton,
  User,
  UserFilled
} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {reactive} from "vue";

const data =reactive({
  //localStorage.getItem('xm-pro-user')这是个字符串所以要parse一下变成JSON
  user:JSON.parse(localStorage.getItem('xm-pro-user'))//缓存数据
})

const logout = () => {
  localStorage.removeItem('xm-pro-user') //清楚当前登录的用户的缓存数据
  location.href='/login' //退出到登录页面
}

const updateUser = () =>{
  data.user=JSON.parse(localStorage.getItem('xm-pro-user'))
}
</script>

<!--<style>-->
<!--el-menu .is-active{-->
<!--  background-color: #e6ecf7 !important;-->
<!--}-->
<!--</style>-->