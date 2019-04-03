<template >
  <div class="row" id="content">
    <div class="col-sm-offset-3 col-sm-6">
      <form class="form-horizontal"@submit.prevent="login">
        <div class="form-group">
          <label for="inputEmail3" class="col-sm-2 control-label">用户名</label>
          <div class="col-sm-10">
            <input type="email" class="form-control" id="inputEmail3" placeholder="用户名" v-model="userName">
          </div>
        </div>
        <div class="form-group">
          <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="inputPassword3" placeholder="密码" v-model="password">
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
              <label>
                <input type="checkbox"> 记住我
              </label>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">登录</button>
            <router-link to="/register">注册</router-link>
          </div>
        </div>
      </form>
    </div>
</div>
</template>


<script type="text/ecmascript-6">
  import {reqLogin} from "../../api";

  export default {
    data()  {
      return{
        userName: '',
        password: ''
      }
    },
    methods:  {
      async login(){
        const result = await reqLogin(this.userName, this.password)
        if(result.code === 200){
          //登录成功去首页
          this.$router.replace('/index')
        }
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  #content
    margin-top 200px
</styles>
