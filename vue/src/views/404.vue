<template>
  <div style="text-align: center">
    <img src="@/assets/404.png" alt="">
    <div><el-button type="primary" style="width: 100px;height: 50px" @click="goHome">返回主页</el-button> </div>
  </div>
</template>

<script setup>
const goHome = () => {
  location.href='/manager/home'
  }
</script>