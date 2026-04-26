<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 240px; margin-right: 5px;" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-button type="primary" @click="handleAdd">新 增</el-button>
    <el-button type="danger" @click="delBatch">批量删除</el-button>
    <el-upload
        style="display: inline-block; margin: 0 10px"
        action="http://localhost:9090/employee/import"
        :show-file-list="false"
        :on-success="importSuccess"
    >
      <el-button type="info">导入</el-button>
    </el-upload>

    <el-button type="success" @click="exportData">导出</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column label="账号" prop="username" />
<!--      表格里面显示图片-->
      <el-table-column label="头像">
        <template #default="scope">
          <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" style="display: block; width: 40px; height: 40px; border-radius: 50%">
        </template>
      </el-table-column>
      <el-table-column label="名称" prop="name" />
      <el-table-column label="性别" prop="sex" />
      <el-table-column label="工号" prop="no" />
      <el-table-column label="年龄" prop="age" />
      <el-table-column label="个人介绍" prop="description" show-overflow-tooltip />
      <el-table-column label="部门" prop="departmentName" />
      <el-table-column label="操作" width="120">
<!--        这个因为官网就这么写的 通过scope拿到行数据-->
        <template #default="scope">
          <el-button @click="handleUpdate(scope.row)" type="primary" :icon="Edit" circle></el-button>
          <el-button @click="del(scope.row.id)"  type="danger" :icon="Delete" circle></el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 15px"> 
<!--      分页组件el-pagination -->
      <el-pagination
          @size-change="load"
          @current-change="load"
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
      />
    </div>

<!--    弹窗组件el-dialog                                        destroy-on-close关闭的时候清空内容-->
    <el-dialog title="员工信息" v-model="data.formVisible"  width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style=" padding-right: 40px;padding-top: 20px">
<!--        el-input输入框-->
        <el-form-item label="账号" prop="username">
          <el-input :disabled="data.form.id" v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="部门" >
          <el-select style="width: 100%" v-model="data.form.departmentId">
            <el-option v-for="item in data.departmentList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              action="http://localhost:9090/files/upload"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
<!--        el-radio-group单选框-->
        <el-form-item label="性别">
          <el-radio-group v-model="data.form.sex">
            <el-radio value="男" label="男"></el-radio>
            <el-radio value="女" label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="工号" prop="no">
          <el-input v-model="data.form.no" autocomplete="off" placeholder="请输入工号"/>
        </el-form-item>
<!--        el-input-number数字框-->
        <el-form-item label="年龄">
          <el-input-number style="width: 180px" :min="18" v-model="data.form.age" autocomplete="off" placeholder="请输入年龄"/>
        </el-form-item>
<!--        type="textarea"多行输入框-->
        <el-form-item label="个人介绍">
          <el-input :rows="3" type="textarea" v-model="data.form.description" autocomplete="off" placeholder="请输入个人介绍"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data =reactive({
  name:null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible:false,
  form: {},
  departmentList: [],
  ids: [],
  rules: {
    username:[
      {required:true, message: '请输入账号', trigger:'blur'}
    ],
    name:[
      {required:true, message: '请输入名称', trigger:'blur'}
    ],
    no:[
      {required:true, message: '请输入工号', trigger:'blur'}
    ]
  }
})

const exportData = () => {
  //导出数据,是通过流的形式下载excel   打开流的链接,浏览器会自动下载文件
  window.open('http://localhost:9090/employee/export')
}

const importSuccess = (res) => {
  if(res.code === '200'){
    ElMessage.success('数据导入成功')
    load()
  } else {
    ElMessage.error(res.msg)
  }

}

const load = () =>{

  //把数据从前端给后端发送请求 后端再controller接收参数数据
  request.get('employee/selectPage',{// ?pageNum=1&pageSize=10
    params: {
      pageNum: data.pageNum,//数据
      pageSize: data.pageSize,//数据
      name: data.name//数据
    }
  }).then(res =>{
    data.tableData=res.data.list
    data.total=res.data.total
  })
}
load()

const handleAvatarSuccess = (res) =>{
  data.form.avatar = res.data
}
const formRef = ref()

const reset =() =>{
  data.name=null;//参数清空
  load()//再加载一次就可以了
}

request.get('/department/selectAll').then(res => {
  data.departmentList = res.data
})

const handleAdd =() =>{
  data.formVisible=true
  data.form={}//防止有脏数据
}

//这里是保存就要发送请求了
//在一个保存方法里面做2个操作 一个是新增 一个是编辑
const save =() =>{
  formRef.value.validate((valid) => {
    //表单校验逻辑
    if(valid) {
      data.form.id ? update() : add()
    }
  })
}

//相当于是新增之后保存 所以请求用post
const add =() =>{
  request.post('employee/add',data.form).then(res =>{//新增的对象没有id
    if(res.code === '200'){
      data.formVisible=false
      ElMessage.success('操作成功')
      load()  //新增后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

//编辑更新用put 编辑是通过id编辑的
const update =() =>{
  request.put('employee/update',data.form).then(res =>{//编辑的对象里包含id
    if(res.code === '200'){
      data.formVisible=false
      ElMessage.success('操作成功')
      load()  //更新后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del =(id) =>{
  ElMessageBox.confirm('删除数据后无法恢复,您确认删除吗?','删除确认',{type: 'warning'}).then(() =>{
    request.delete('employee/deleteById/'+id).then(res =>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()  //删除后一定要重新加载最新的数据
      } else {
        ElMessage.error(res.msg)
      }
    }).catch()
  })

}
//该方法是编辑 那应该是put
const handleUpdate =(row) =>{
  data.form=JSON.parse(JSON.stringify(row))//深拷贝一个新的对象 用于编辑 这样就不会影响行对象
  data.formVisible=true
}

const handleSelectionChange =(rows) =>{//返回所有选中的行对象数组 rows是个数组 handleSelectionChange方法名是官网的
  //从选中的行数组里取出所有行的id组成一个新的数组
  data.ids = rows.map(row =>row.id)
  console.log(data.ids)
}

const delBatch =() =>{
  if(data.ids.length === 0){
    ElMessage.warning('请选择数据')
    return 
  }
  ElMessageBox.confirm('删除数据后无法恢复,您确认删除吗?','删除确认',{type: 'warning'}).then(() =>{
                                                //{}里是js参数对象
    request.delete('employee/deleteBatch',{ data: data.ids }).then(res =>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()  //删除后一定要重新加载最新的数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}
</script>