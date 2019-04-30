<template>
  <div class="container">
    <div class="row"><Head></Head></div>
    <br><br>

    <div class="row">

      <!--导航栏-->
      <div class="col-sm-3">
        <h4>
          <div class="list-group" style="background: none">
            <router-link to="/index" type="button" class="list-group-item">返回首页</router-link>
            <router-link to="/user/userInfo" type="button" class="list-group-item">个人信息</router-link>
            <router-link to="/user/historyPlay" type="button" class="list-group-item">历史记录</router-link>
            <router-link to="/user/gameHelp" type="button" class="list-group-item">游戏帮助</router-link>
           <!-- <router-link to="/user/feedback" type="button" class="list-group-item">意见反馈</router-link>
        -->  </div>
        </h4>
      </div>


      <div class="col-sm-9 " style="overflow-y: auto; overflow-x: hidden;
      height: 500px; word-break:break-all">
        <router-view></router-view>
      </div>

    </div>
    <div class="row"><Footer/></div>
  </div>
</template>

<script>
  import Footer from '../../components/Footer/Footer'
  import Head from '../../components/Head/Head'


  export default {
    name: "User",
    components: {
      Footer,
      Head
    }
  }
</script>

<style  lang="stylus" rel="stylesheet/stylus">

</style>
