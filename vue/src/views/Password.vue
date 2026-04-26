<template>
  <div class="card" style="width: 50%; padding: 40px 20px">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="100px" style=" padding-right: 40px;padding-top: 20px">
      <!--        el-input输入框-->
      <el-form-item label="原密码" prop="password">
        <el-input show-password v-model="data.form.password" autocomplete="off" placeholder="请输入原密码"/>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input show-password v-model="data.form.newPassword" autocomplete="off" placeholder="请输入新密码"/>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword" required>
        <el-input show-password v-model="data.form.confirmPassword" autocomplete="off" placeholder="请再次确认新密码"/>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button @click="updatePassword" type="primary" style="padding: 20px 30px;">立即修改</el-button>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const formRef = ref()
const validatePass = (rule, value, callback) => { //自定义校验规则
  if (!value) { //valid = true：表单中所有配置了 rules 校验规则的字段，均符合定义的规则
    callback(new Error('请再次确认新密码'))
  } else if (value !== data.form.newPassword) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}
const data = reactive({
  user:JSON.parse(localStorage.getItem('xm-pro-user')),
  form: {},
  rules: {

    password: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
    ],
    newPassword: [
      {required: true, message: '请输入新密码', trigger: 'blur'}
    ],
    confirmPassword: [
      {validator: validatePass, trigger: 'blur'} //自定义校验规则
    ],
  }
})

const updatePassword = () =>{
  data.form.id = data.user.id //取出用户缓存里的id
  data.form.role = data.user.role //取出用户缓存里的id
  /*
  调用 validate() 时，会主动校验整个表单。如果不加 valid 判断，即使用户未输入原密码、新密码与确认密码不一致，
  代码依然会发起 /updatePassword 请求，导致无效的后端调用。
   */
  formRef.value.validate((valid) => {
    if(valid){ //拦截 判定表单所有字段是否整体通过校验 如果valid通过了才发请求
      request.put('/updatePassword',data.form).then(res =>{
        if(res.code === '200'){
          ElMessage.success('修改成功')
          //修改成功后先清空缓存数据,然后再返回到登录界面
          localStorage.removeItem('xm-pro-user')
          setTimeout(() =>{
            location.href = '/login'
          },500)

        } else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>