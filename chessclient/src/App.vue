<template>
  <div id="app" class="container">
    <div class="row">
      <Head v-show="$route.meta.showHead"></Head>
    </div>
    <div class="row">
        <router-view></router-view>
    </div>
    <div class="row">
      <Footer v-show="$route.meta.showFooter"></Footer>
    </div>
  </div>
</template>

<script>
  import Footer from './components/Footer/Footer'
  import Head from './components/Head/Head'
  import {mapActions,mapState} from 'vuex'
  export default {
    mounted() {
      this.getUserInfo()
    },
    methods:  {
      ...mapActions(['getUserInfo']),
    },
    components: {
      Footer,
      Head
    }

  }
</script>

<style>

</style>
