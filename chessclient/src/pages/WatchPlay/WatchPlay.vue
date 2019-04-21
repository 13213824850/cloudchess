<template>
  <div  class="row" v-if="cheses != null && cheses.length != 0">
    <!--对局棋盘-->
    <div class=" col-sm-8" id="chess" ref="qipan">
      <!--画个棋盘加visibility属性是为其咱个地方-->
      <div class="row" v-for="x in 10" style="height: 56px">
           <span :id="(y-1)+':' + (x-1)" v-for="y in 9" @click="moveChese($event)">
             <img :src="  cheses[y-1][x-1] != 0 ? baseImageUrl + cheses[y-1][x-1] +'.png' :baseImageUrl +'1.png'"
                  :style="{width:'65px', visibility : cheses[y-1][x-1] == 0 ? 'hidden':'',} "
             /></span>
      </div>
    </div>

    <!--消息-->
    <div class="col-sm-4">
      <!--剩余时间-->
      <div class="row">剩余时间 {{overPlusTime}}秒</div>
      <!--状态-->
      <div class="row">状态： {{gameState}}</div>


      <div class="row">
        双方信息：
        <div class="row">
          <div class="row">
            <span class="glyphicon glyphicon-user" style="color: red"></span>
            <span>{{rank.nickName}}</span>
            <span>{{rank.stage }}<span>{{rank.star}}</span></span>
          </div>
          <h3>vs</h3>
          <div class="row">
            <span class="glyphicon glyphicon-user"></span>
            <span>{{otherRank.nickName}}</span>
            <span>{{otherRank.stage }}<span>{{otherRank.star}}</span></span>
          </div>
          <div class="row"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import rankUtil from '../../utils/rankUtil'
  import {reqGetRank} from "../../api";

  export default {
    name: "WatchPlay",
    data() {
      return {
        baseImageUrl: 'http://localhost:8080/static/images/',
        codeIndex: [], //棋子的位置信息
        imgstr: './images/2.png',
        cheses: [],
        overPlusTime: 0, //剩余时间
        gameState: '',//游戏状态
        rank: {},//我方信息
        otherRank: {}, //对方信息
        overPlusTimerId: null /*计时器*/
      }
    },
    created() {
      this.initCheses()
    },
    computed: {
      ...mapState(['cheseIndex'])
    },
    methods: {
      //初始化数据
      //计算棋子位置初始化
      initCheses() {
        //显示双方信息
        this.cheses = this.cheseIndex.map.cheses
        this.showRanks(this.cheseIndex.userName, this.cheseIndex.oppUserName)
      },
      //显示对局双方rank
      async showRanks(userName, oppUserName) {
        let result = await reqGetRank(userName)
        this.rank = rankUtil.getRankInfo(result)
        let oppresult = await reqGetRank(oppUserName)
        this.otherRank = rankUtil.getRankInfo(oppresult)
      },
      //显示还剩多少秒
      showOverPlushTime() {
        this.overPlusTimerId = setInterval(() => {
          if (this.overPlusTime === 0) {
            this.turnMe = false
            clearInterval(this.overPlusTimerId)
          } else {
            this.overPlusTime--
          }
        }, 1000)
      },
    },
    watch: {
      //监听接受的数据根改棋盘
      cheseIndex: function (cheseIndex) {
        this.overPlusTime = cheseIndex.ramainTime
        clearInterval(this.overPlusTimerId)
        this.showOverPlushTime()
        this.cheseIndex = cheseIndex
        let codeIndex = cheseIndex.codeIndex
        this.$set(this.cheses[codeIndex.startX], codeIndex.startY, 0)
        this.cheses[codeIndex.startX][codeIndex.startY] = 0
        this.cheses[codeIndex.endX][codeIndex.endY] = codeIndex.code

        //查看游戏是否结束
        if (cheseIndex.gameState !== 400) {
          this.gameState = '游戏结束'
        }
        this.gameState = '对局中'
      }

    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  #chess
    background url("./images/chess.jpg") no-repeat
    background-size auto
    height 632px
</style>
