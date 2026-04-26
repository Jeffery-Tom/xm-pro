<template>

  <div>
    <div style="margin-bottom: 20px">
      <RouterLink to="test">跳转到Test.vue</RouterLink>|
      <a href="test">跳转到Test.vue</a>
    </div>
  </div>

  <div style="margin: 20px">
    <el-button type="primary" @click="router.push('/manager/test')">push能返回</el-button>
    <el-button type="primary" @click="router.replace('/manager/test')">replace无法返回</el-button>
  </div>

  <div style="margin: 20px">
<!--    <el-button type="primary" @click="router.push('/test?id=2&name=TJ')">路由传参到Test接收</el-button>-->
<el-button type="primary" @click="router.push({path:'/manager/test', query:{ id:2,name:'TJ'}})">路由传参到Test接收</el-button>
  </div>
  <div style="margin: 20px">
    <el-button type="warning" @click="surprise">点我有惊喜!</el-button>
  </div>
  <div>
    <div style="margin: 20px 0">
      <el-input v-model="data.input" style="width: 240px" placeholder="请输入内容" :prefix-icon="Search"></el-input>{{data.input}}
      <el-input style="width: 200px" :suffix-icon="Calendar"></el-input>
      <el-input type="textarea" v-model="data.descr" style="width: 300px" placeholder="一段描述"></el-input>
    </div>
  </div>

  <div style="margin: 20px 0">
    <el-select v-model="data.value" placeholder="请选择水果"  style="width: 240px">
      <el-option
          v-for="item in data.options"
          :key="item"
          :label="item"
          :value="item">
      </el-option>
    </el-select>
  </div>

  <div style="margin:20px 0">
    <el-carousel height="400px" style="width:500px">
      <el-carousel-item v-for="item in data.imgs" :key="item">
        <img style="width: 100%;height: 400px" :src="item" alt="">
      </el-carousel-item>
    </el-carousel>
  </div>

  <div style="margin:20px 0">
    <el-date-picker
        v-model="data.date"
        type="date"
        placeholder="选择日期时间"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD">

    </el-date-picker>{{data.date}}
  </div>

  <div style="margin:20px 0">
    <el-table :data="data.tableData" style="width: 100%" stripe >
      <el-table-column prop="date" label="日期" width="180"/>
      <el-table-column prop="name" label="姓名" width="180"/>
      <el-table-column prop="address" label="地址"/>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" circle @click="edit(scope.row)">
            <el-icon><Edit /></el-icon>
          </el-button>
          <el-button type="danger" circle @click="del(scope.row.id)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </template>

      </el-table-column>
    </el-table>
  </div>
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="data.currentPage"
        :page-size="data.pagesize"
        :page-sizes="[5, 10, 15, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="50">
    </el-pagination>
    <!--    <el-pagination-->
    <!--        @size-change="handleSizeChange"-->
    <!--        @current-change="handleCurrentChange"-->
    <!--        :current-page="currentPage4"-->
    <!--        :page-sizes="[100, 200, 300, 400]"-->
    <!--        :page-size="100"-->
    <!--        layout="total, sizes, prev, pager, next, jumper"-->
    <!--        :total="400">-->
    <!--    </el-pagination>-->
  </div>
  <el-dialog v-model="data.dialogVisible" title="编辑行对象"  width="500">
    <div style="padding: 20px">
      <div style="margin-bottom: 10px">日期: {{data.row.date}}</div>
      <div style="margin-bottom: 10px">名称: {{data.row.name}}</div>
      <div>地址: {{data.row.address}}</div>
    </div>
  </el-dialog>

</template>

<script setup>

import {reactive} from "vue";
import {Calendar, Delete, Search} from "@element-plus/icons-vue";
import lun1 from '@/assets/lun1.png'
import lun2 from '@/assets/lun2.jpg'
import lun3 from '@/assets/mou.png'
import router from "@/router/index.js";
import request from "@/utils/request.js";

const data = reactive({
  input: 'TJJJ',
  descr: '有款游戏越打越年轻这就是手机和平精英这里没有人叫你老弟首先你得内个内个哈达内个内个哈琼琼哈达琼琼哈',
  value: '',
  options: ['苹果','香蕉','西瓜'],
  imgs: [lun1,lun2,lun3],
  date:'',
  tableData:[
    {id:1 ,date:'2026/4/11',name:'TJ',address:'ndky'},
    {id:2 ,date:'2026/4/12',name:'WYL',address:'js'},
    {id:3 ,date:'2026/4/13',name:'bro',address:'jx'},
  ],
  currentPage:1,
  pagesize:5,
  dialogVisible: false,
  row:null,
  employeeList: []
})

request.get(`/employee/selectAll`).then(res =>{
   console.log(res)
  data.employeeList = res.data //res.data就是员工列表的数据 是一个数组
})

const del = (id) =>{
  alert("删除ID="+id+"的数据")
}
const edit =(row) =>{
  data.row=row
  data.dialogVisible=true
}
const surprise =() =>{
  alert("唐健好帅a!!当然了低调低调~")
}

data.tableData=data.tableData.splice(0,5)



</script>