<template>
  <div class="row" id="content">
    <div class="col-sm-offset-3 col-sm-6">
      <form class="form-horizontal" @submit.prevent="register">
        <div class="form-group">
          <label for="inputEmail3" class="col-sm-2 control-label">用户名</label>
          <div class="col-sm-10">
            <input type="email" class="form-control" id="inputEmail3" placeholder="用户名" v-model="userName">
          </div>
        </div>
        <div class="form-group">
          <label for="inputEmail3" class="col-sm-2 control-label">昵称</label>
          <div class="col-sm-10">
            <input class="form-control" placeholder="请输入昵称" v-model="nickName">
          </div>
        </div>
        <div class="form-group">
          <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="inputPassword3" placeholder="密码" v-model="passwordSure">
          </div>
        </div>
        <div class="form-group">
          <label for="inputPassword3" class="col-sm-2 control-label">确认密码</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" placeholder="请再次输入密码" v-model="password">
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
  </div>
</template>

<script type="text/ecmascript-6">
  import {reqRegister} from "../../api";

  export default {
    data() {
      return {
        userName: '',
        nickName: '',
        password: '',
        passwordSure: '',
      }
    },
    methods:  {
      register(){
        if(this.userName==='' || this.nickName === ''){
          alert("用户名或密码不能为空");
          return
        }
        if(this.password != this.passwordSure){
          alert('密码不一致')
          return
        }
        const result = reqRegister(this.userName, this.password, this.nickName)
        if(result.code == 200){
          console.log("注册成功")
        }else{
          console.log("注册失败"+result)
        }

      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  #content
    margin-top 100px
</style>
