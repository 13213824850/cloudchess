<template>
  <div>
  <center>
    <h3>
      <div v-if="code === '200'">
        <router-link to="/login">注册成功请登录</router-link>
      </div>
      <div v-else>
        <span>注册失败</span>
      </div>
    </h3>
  </center>
  </div>

</template>

<script>
  export default {
    name: "UserAc",
    data(){
      return{
        text:'',
        code:''
      }
    },
    created() {
      this.showMessage()
    },
    methods: {
      showMessage() {
        let split = this.$route.fullPath.split('?')
        let msg
        if(split.length != null && split.length >= 2){
          this.code = split[1]
        }
      }
    }

  }
</script>

<style scoped>

</style>
