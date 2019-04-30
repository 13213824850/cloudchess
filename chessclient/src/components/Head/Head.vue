<template>
  <div class="row">
   <div class=" col-sm-1" >
        <img src="../../../static/images/1.png" id="demo"  alt="" width="100" height="90" >
    </div>
    <div class="col-sm-offset-2 col-sm-5"><h3><p id="p">中国在线象棋对战</p></h3></div>
    <div class="col-sm-3 col-sm-offset-1">
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
  #p {
    font-family: 'Audiowide';
    text-align: center;
    color: #00a67c;
    -webkit-transition: all 1.5s ease;
    transition: all 1.5s ease;
  }
  #p:hover {
    color: #fff;
    -webkit-animation: Glow 1.5s ease infinite alternate;
    animation: Glow 1.5s ease infinite alternate;

  }
  @-webkit-keyframes Glow {
    from {
      text-shadow: 0 0 10px #fff,
        0 0 20px #fff,
        0 0 30px #fff,
        0 0 40px #00a67c,
        0 0 70px #00a67c,
        0 0 80px #00a67c,
        0 0 100px #00a67c,
        0 0 150px #00a67c;
    }
    to {
      text-shadow: 0 0 5px #fff,
        0 0 10px #fff,
        0 0 15px #fff,
        0 0 20px #00a67c,
        0 0 35px #00a67c,
        0 0 40px #00a67c,
        0 0 50px #00a67c,
        0 0 75px #00a67c;
    }
  }
  @keyframes Glow {
    from {
      text-shadow: 0 0 10px #fff,
        0 0 20px #fff,
        0 0 30px #fff,
        0 0 40px #00a67c,
        0 0 70px #00a67c,
        0 0 80px #00a67c,
        0 0 100px #00a67c,
        0 0 150px #00a67c;
    }
    to {
      text-shadow: 0 0 5px #fff,
        0 0 10px #fff,
        0 0 15px #fff,
        0 0 20px #00a67c,
        0 0 35px #00a67c,
        0 0 40px #00a67c,
        0 0 50px #00a67c,
        0 0 75px #00a67c;
    }
  }
</style>
<style type="text/css">

  #demo {
    position: absolute;
    transform: rotate(0deg);
    transition: transform 1s linear;
  }
  #demo:hover {
    transform: rotate(360deg);
  }
</style>
