<template>

  <div id="app">
      <div class="container">
        <div class="row">
          <Head v-show="$route.meta.showHead"></Head>
        </div>
        <br>
        <div class="row">
          <router-view></router-view>
        </div>
        <br>
        <div class="row">
          <Footer v-show="$route.meta.showFooter"></Footer>
        </div>
      </div>
  </div>
</template>

<script>
  import Footer from './components/Footer/Footer'
  import Head from './components/Head/Head'
  import {mapActions, mapState} from 'vuex'

  export default {
    mounted() {
      this.getUserInfo()
    },
    methods: {
      ...mapActions(['getUserInfo']),
    },
    components: {
      Footer,
      Head
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  #app
    background:  url(../static/images/beijin.jpg);
    background-size: 100% 100%;
    /*height: auto;
    position: fixed;
    width: 100%*/
</style>
