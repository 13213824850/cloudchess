<template>
  <div class="row">
    <div class="col-sm-offset-3 col-sm-5"><h3>中国在线象棋对战</h3></div>
    <div class="col-sm-4">
      <h4>
        <div v-if="userInfo.nickName">用户名:
          <router-link to="/user">{{userInfo.nickName}}</router-link>
          <a @click="logOut">注销</a>&nbsp;&nbsp;&nbsp;


        </div>
        <div v-else>
          <router-link to="/login">登录</router-link>
          |
          <router-link to="/register">注册</router-link>
        </div>
      </h4>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import {mapState,mapActions} from 'vuex'
  import {reqLogOut} from "../../api"
  import cookie from 'vue-cookie'

  export default {
    computed: {
      ...mapState(['userInfo']),
    },
    methods: {
      ...mapActions(['deleteUserInfo']),
      //注销
      async logOut() {
        const result = await reqLogOut();
        console.log(result)
        if (result.code === 200) {
          //返回登录页面
          cookie.delete('CHESS_TOKEN')
          this.deleteUserInfo()
          this.$router.replace('/login')
        }
      },
    },


  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
</style>
