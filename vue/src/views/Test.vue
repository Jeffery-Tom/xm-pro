<template>
  <div>
    <el-button type="primary" @click="router.back()">返回主页</el-button>
  </div>
  <div>
    <div style="font-size: 20px;font-weight: bold; color:red;text-decoration: underline;font-style: italic;margin-bottom: 20px">
      欢迎来到踢街的项目哦!
    </div>
  </div>

  <div>
    {{data.a}}
    {{data.b}}
  </div>

  <div style="margin: 30px">
    <el-button @click="clickData" :style="{color:'red'}">Default</el-button>
  </div>

  <div style="margin:30px">
    <el-icon size="20">
      <Edit />
    </el-icon>
  </div>

  <el-button type="danger" :icon="Delete" circle />
</template>


<script setup>

import {reactive} from "vue";
import {Delete} from "@element-plus/icons-vue";
import router from "@/router/index.js";

const data=reactive({
  id: router.currentRoute.value.query.id,//接收参数(获取到query对象就能接受到传过来的数据)
  name: router.currentRoute.value.query.name,//接收参数
  a:123,
  b:'tj'
})

const clickData = () =>{
  alert("Element-plus")
}
</script>


