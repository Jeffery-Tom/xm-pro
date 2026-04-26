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
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column label="名称" prop="name" />
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
    <el-dialog title="部门信息" v-model="data.formVisible"  width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style=" padding-right: 40px;padding-top: 20px">
<!--        el-input输入框-->
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
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
  ids: [],
  rules: {
    name:[
      {required:true, message: '请输入名称', trigger:'blur'}
    ]
  }
})

const load = () =>{

  //把数据从前端给后端发送请求 后端再controller接收参数数据
  request.get('department/selectPage',{// ?pageNum=1&pageSize=10
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

const formRef = ref()

const reset =() =>{
  data.name=null;//参数清空
  load()//再加载一次就可以了
}

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
  request.post('department/add',data.form).then(res =>{//新增的对象没有id
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
  request.put('department/update',data.form).then(res =>{//编辑的对象里包含id
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
    request.delete('department/deleteById/'+id).then(res =>{
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
    request.delete('department/deleteBatch',{ data: data.ids }).then(res =>{
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