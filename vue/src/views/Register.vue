<template>
<!--  该div是整个页面容器-->
  <div class="login-container">
<!--    该div是登录的框框(框架)-->
    <div class="login-box">
<!--      border-radius是设置圆角 text-align:center字体居中 font-weight: bold加粗字体-->
<!--      该div是登录框框里的内容-->
      <div style="padding: 30px; background-color: white; margin-left: 100px; border-radius: 5px;box-shadow: 0 0 10px rgba(0,0,0,0.2)">
<!--                           :model表示让el-form这个表单和data.form这个对象绑定起来-->
<!--                            在 el-input 里输入的内容，会自动同步到 data.form 里-->
<!--                          简单说：它是表单数据的 “容器”，让你能更方便地管理和提交表单。-->
<!--        所以 data.form 本质上就是一个被 Vue 监听的 JS 对象，用来管理表单数据。-->
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 350px">
          <div style="margin-bottom: 30px; font-size: 24px;text-align: center; color: #02457e; font-weight: bold">欢 迎 注 册</div>
          <el-form-item prop="username">
<!--            在代码里修改 data.form.username，页面上的输入框也会跟着变化-->
            <el-input v-model="data.form.username" placeholder="请输入账号" prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="no">
            <el-input show-password v-model="data.form.no" placeholder="请输入工号" prefix-icon="Lock"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password v-model="data.form.password" placeholder="请输入密码" prefix-icon="Lock"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input show-password v-model="data.form.confirmPassword" placeholder="请确认密码" prefix-icon="Lock"></el-input>
          </el-form-item>
          <div style="margin-bottom: 20px">
            <el-button @click="register" size="large" style="width: 100%" type="primary">注 册</el-button>
          </div>
<!--         text-align让div往右显示                                        text-decoration去掉a标签超链接的下划线-->
          <div style="text-align: right">已有账号? 请 <a style="color: #247aea;text-decoration: none " href="/login" >登录</a></div>
        </el-form>
      </div>

    </div>
  </div>
</template>

<script setup>

import {reactive, ref} from "vue";
import {User,Lock} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次确认密码'))
  } else if (value !== data.form.password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const data = reactive({
  form: {},
  rules: {
    // 记得在表单el-form-item上加prop='username'
    username:[
      {required: true, message: '请输入账号', trigger: 'blur' }
    ],
    no:[
      {required: true, message: '请输入工号', trigger: 'blur' }
    ],
    // 记得在表单el-form-item上加prop='password'
    password:[
      {required: true, message: '请输入密码', trigger: 'blur' }
    ],
    confirmPassword: [
      {validator: validatePass, trigger: 'blur'}
    ]
  }
})
//1
const formRef = ref()//表单校验流程 1定义ref 2定义rules 3在data里写一下rules数据 4定义prop


  //表单校验逻辑
const register =() => {
  formRef.value.validate((valid) => {
    //表单校验逻辑
    if(valid) {
      request.post('/register',data.form).then(res =>{
        if(res.code === '200'){
          ElMessage.success('注册成功')
          setTimeout(() => {
            location.href = '/login' //登陆成功跳转主页
          },500)

        }else{
          //ElMessage 是 Element Plus 提供的消息提示组件 就是网页上方的那个消息提示
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const rules =() => {

}

</script>

<!--表示当地生效-->
<!--100vh表示占整个页面高度-->
<!--overflow: hidden表示隐藏超出页面元素部分-->
<!--background-size: cover自动覆盖整个页面-->
<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/james.jpg");
  background-size: 100%,100%;
  background-position: -50px -100px;
}

.login-box {
  display: flex;
  align-items: center;
  width: 50%;
  height: 100%;
  right: 0;
  position: absolute;
}
</style>