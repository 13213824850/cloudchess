<template>
  <div class="row" style="margin-left: 3%">
  <h3>个人中心</h3>
   <h4>
     <div class="row">
       <div class="col-sm-6">账号：{{userInfo.userName}}</div>
       <div class="col-sm-6">昵称：{{userInfo.nickName}}</div>
     </div>
  <br>
     <div class="row">
       <div class="col-sm-6">性别: {{userInfo.sex == 0 ? '男':'女'}}</div>
       <div class="col-sm-6">年龄:  {{userInfo.age}}</div>
     </div><br>
     <div class="row">
       <div class="col-sm-6">段位：
       <span :style="{color: rank.colorValue}">{{rank.stage}}
         <span v-for="count in rank.star">
           <span class="glyphicon glyphicon-star" :id="count"></span>
         </span></span>
       </div>
       <div class="col-sm-6">胜率: {{rank.winRate + '%'}}</div>
     </div><br>
     <div class="row">
       <div class="col-sm-6">赢场次：{{rank.windCount}}</div>
       <div class="col-sm-6">输场次: {{rank.failCount}}</div>
     </div><br>
     <div class="row">
       <div class="col-sm-6">总场次：{{rank.countPlay}}</div>
       <div class="col-sm-6">创建日期: {{formateTime()}}</div>
     </div>
   </h4>

  </div>


</template>

<script>
  import {mapState} from 'vuex'
  import {reqGetRank} from "../../../api/index";
  import rankUtil from '../../../utils/rankUtil'
  import moment from 'moment'
  export default {
    name: "UserInfo",
    data() {
      return {
        rank: {}, //段位信息
      }
    },
    created() {
      //获取用户信息初始化
      this.initData()
    },
    computed: {
      ...mapState(['userInfo'])
    },
    methods: {
      //init
      async initData() {
        //获取段位
        const result = await reqGetRank(this.userInfo.userName)
        if (result.code === 200) {
           this.rank = rankUtil.getRankInfo(result)
        } else {
          alert(result.message)
        }

      },

      //时间个数话
      formateTime(){
        return moment(this.userInfo.createTime).locale('zh_cn').toNow()
      }
    }
  }
</script>

<style scoped>

</style>
