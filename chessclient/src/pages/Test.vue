<template>
  <div id="vm">
    <process-tip></process-tip>
  </div>
</template>

<script type="text/ecmascript-6">
  import moment from 'moment'
  import ProcessTip from '../components/AlterTip/ProcessTip'
  export default {
    data() {
      return {
        playTime: '',
        startTime: ''
      }
    },
    components:{
      ProcessTip
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


