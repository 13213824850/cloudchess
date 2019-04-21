<template >
  <div class="row" style="height: 300px; margin-top: 150px">
    <div class="col-sm-offset-3 col-sm-8">
      <form class="form-horizontal"@submit.prevent="login">
        <div class="form-group">
          <label for="inputEmail3" class="col-sm-2 control-label">用户名</label>
          <div class="col-sm-4">
            <input type="email" class="form-control" id="inputEmail3" placeholder="用户名" v-model="userName">
          </div>
          <div>
            <span v-if="userName == ''">用户名不能未空</span>
          </div>
        </div>
        <div class="form-group">
          <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
          <div class="col-sm-4">
            <input type="password" class="form-control" id="inputPassword3" placeholder="密码" v-model="password">
          </div>
          <div>
            <span v-if="password == ''">密码不能未空</span>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">登录</button>
            <router-link to="/register">注册</router-link>
          </div>
        </div>
      </form>
      <AlertTip :alertText="alertText" v-show="alertShow" @closeTip="closeTip"/>
    </div><br><br><br><br><br>
</div>
</template>


<script type="text/ecmascript-6">
  import {reqLogin} from "../../api";
  import {mapActions} from 'vuex'
  import AlertTip from '../../components/AlterTip/AlterTip'
  export default {
    data()  {
      return{
        userName: '',
        password: '',
        alertText:'',
        alertShow:false
      }
    },
    components: {
      AlertTip
    },
    methods:  {
      ...mapActions(['getUserInfo']),
      async login(){
        if(this.userName == '' || this.password == ''){
            return
        }
        const result = await reqLogin(this.userName, this.password)
        if(result.code === 200){
          //登录成功去首页
         //更新信息
          this.getUserInfo()
          this.$router.push('/index')
        }else{
          this.alertShow = true
          this.alertText = result.message
        }
      },

      closeTip () {
        this.alertShow = false
        this.alertText = ''
      },
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</styles>
