<template>
  <div id="vm">
    时间：{{playTime +''}}
  </div>
</template>

<script type="text/ecmascript-6">
  import moment from 'moment'
  export default {
    data() {
      return {
        playTime: '',
        startTime: ''
      }
    },
    mounted() {
      this.showTime()
    },
    methods: {
      showTime() {
        this.startTime = moment(new Date());
         var that = this;
         setInterval(function () {
          that.playTime = moment(that.startTime).locale('zh_cn').toNow()
        }, 1000)
      },

    }
  }

</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>


