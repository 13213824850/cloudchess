<template>
  <div class="row" id="content" style="height: 300px; margin-top: 150px">
    <div class="col-sm-offset-3 col-sm-9">
      <form class="form-horizontal" @submit.prevent="register">
        <div class="form-group">
          <label for="inputEmail3" class="col-sm-2 control-label">用户名</label>
          <div class="col-sm-4">
            <input type="email" class="form-control" id="inputEmail3" placeholder="用户名" v-model="userName">
          </div>
          <div>
            <span v-if="userName == ''">邮箱不能未空</span>
          </div>
        </div>
        <div class="form-group">
          <label for="inputEmail3" class="col-sm-2 control-label">昵称</label>
          <div class="col-sm-4">
            <input class="form-control" placeholder="请输入昵称" v-model="nickName">
          </div>
          <div>
            <span v-if="nickName == ''">昵称不能未空</span>
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
          <label for="inputPassword3" class="col-sm-2 control-label">确认密码</label>
          <div class="col-sm-4">
            <input type="password" class="form-control" placeholder="请再次输入密码" v-model="passwordSure">
          </div>
          <div>
            <span v-if="messagePassword != ''">{{messagePassword}}</span>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">注册</button>
            <button type="reset" class="btn btn-default">重置</button>
            <router-link to="/login">返回登录</router-link>
          </div>
        </div>

      </form>
    </div>
      <AlertTip :alertText="alertText" v-show="alertShow" @closeTip="closeTip"/>
  </div>

</template>

<script type="text/ecmascript-6">
  import {reqRegister} from "../../api";
  import AlertTip from '../../components/AlterTip/AlterTip'
  export default {
    data() {
      return {
        userName: '',
        nickName: '',
        password: '',
        passwordSure: '',
        messagePassword:'',
        alertText: '', // 提示文本
        alertShow: false, // 是否显示警告框
      }
    },
    components: {
      AlertTip
    },
    methods:  {
      async register(){
        if(this.userName==='' || this.nickName === ''){
          return
        }
        if(this.password != this.passwordSure){
          this.messagePassword = '两次密码不一致'
          return
        }
        const {userName, password, nickName} = this
        const result = await reqRegister({userName, password, nickName})
        if(result.code === 200){
          this.alertShow = true
          this.alertText = '注册成功请前往邮箱激活'
        }else{
          this.alertShow = true
          this.alertText = result.message
        }

      },
      // 关闭警告框
      closeTip () {
        this.alertShow = false
        this.alertText = ''
      },
      showAlert(alertText) {
        this.alertShow = true
        this.alertText = alertText
      },
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  #content
    margin-top 100px
</style>
