<template>
  <div>
    <div style="display: flex; grid-gap: 10px">
      <div class="card" style="padding: 20px; flex: 1;height: 400px" id="bar"></div>
      <div class="card" style="padding: 20px; flex: 1;height: 400px" id="line"></div>
    </div>
  </div>
</template>



<script setup>
import {reactive, onMounted} from "vue";
import * as echarts from 'echarts';
import request from "@/utils/request.js";



const data =reactive({

})

const barOption = {
  title: {
    text: '各部门员工数量'
  },
  tooltip: {},
  legend: {
    data: ['人数']
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '人数',
      type: 'bar',
      data: [],
      itemStyle: {
        normal: {
          color: function (params) {
            let colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea'];
            return colors[params.dataIndex % colors.length];
          }
        }
      },
    }
  ]
};

// onMounted 表示页面的所有dom元素都初始化完成了
onMounted(() => {
  // 基于准备好的dom，初始化echarts实例
  const barChart = echarts.init(document.getElementById('bar'));

  request.get('/barData').then(res =>{
    barOption.xAxis.data = res.data.department //横轴数据
    barOption.series[0].data = res.data.count //纵轴数据
    // 使用刚指定的配置项和数据显示图表。
    barChart.setOption(barOption);
  })
});

</script>


<style scoped>

</style>