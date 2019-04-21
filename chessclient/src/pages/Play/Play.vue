<template>

  <div class="row" v-if="this.mySocket != null && cheses != null && cheses.length != 0">
    <!--对局棋盘-->
    <div class=" col-sm-8" id="chess" ref="qipan">
      <!--画个棋盘加visibility属性是为其咱个地方-->
      <div class="row" v-for="x in 10" style="height: 56px" v-if="!isCodeZ">
           <span :id="(y-1)+':' + (x-1)" v-for="y in 9" @click="moveChese($event)">
             <img :src="  cheses[y-1][x-1] != 0 ? baseImageUrl + cheses[y-1][x-1] +'.png' :baseImageUrl +'1.png'"
                  :style="{width:'65px', visibility : cheses[y-1][x-1] == 0 ? 'hidden':'',} "
             /></span>
      </div>
      <div class="row" v-for="x in 10" style="height: 56px" v-if="isCodeZ">
          <span :id="(y-1)+':' + (10-x)" v-for="y in 9" @click="moveChese($event)">
             <img :src="  cheses[y-1][10-x] != 0 ? baseImageUrl + cheses[y-1][10-x] +'.png' :baseImageUrl +'1.png'"
                  :style="{width:'65px', visibility : cheses[y-1][10-x] == 0 ? 'hidden':'',} "
             /></span>
      </div>
    </div>

    <!--消息-->
    <div class="col-sm-4">
      <!--开局时间-->
      <div class="row">对局时间：{{playIngTime + ''}}</div>
      <!--剩余时间-->
      <div class="row">剩余时间 {{overPlusTime}}秒</div>
      <!--状态-->
      <div class="row">状态： {{gameState}}</div>


      <div class="row">
        双方信息：
        <div class="row">
          <div class="row">
            <!--sex-->
            <span class="glyphicon glyphicon-user" :style="{color: userInfo.sex === 0 ? 'black' : 'red'}"></span>
            <span>{{rank.nickName}}</span>
            <span>{{rank.stage }}<span>{{rank.star}}</span></span>
          </div>
          <h3>vs</h3>
          <div class="row">
            <!--sex-->
            <span class="glyphicon glyphicon-user" :style="{color: userInfo.sex === 0 ? 'black' : 'red'}"></span>
            <span>{{otherRank.nickName}}</span>
            <span>{{otherRank.stage }}<span>{{otherRank.star}}</span></span>
          </div>
          <div class="row"></div>
        </div>
      </div>
    </div>
  </div>
  <div class="row" v-else style="align-content:center">
    <center style="margin-top: 200px;">
      <h2>
        <router-link to="/index">重新连接</router-link>
      </h2>
    </center>
  </div>

</template>

<script type="text/ecmascript-6">
  import {reqPlayWs, reqGetOtherRank, reqGetRank} from "../../api";
  import {mapState} from 'vuex'
  import moment from 'moment'
  import rankUtil from '../../utils/rankUtil'

  export default {
    data() {
      return {
        baseImageUrl: 'http://localhost:8080/static/images/',
        path: reqPlayWs,
        touchCode: false,
        codeIndex: [], //棋子的位置信息
        userName: '',//个人账号用户判断拿的是不是自己的棋子
        isCodeZ: true, //我方棋子是不是大于0
        imgstr: './images/2.png',
        codeSizeWidth: null,//棋子的大小
        codeSizeheight: null,//棋子的大小
        cheses: [],
        startTime: '',
        playIngTime: '',
        overPlusTime: 0, //剩余时间
        turnMe: false, //是否轮到我
        gameState: '',//游戏状态
        rank: {},//我方信息
        otherRank: {}, //对方信息
        overPlusTimerId: null /*计时器*/
      }
    },

    mounted() {
      this.initCheses()
    },
    computed: {
      ...mapState(['cheseIndex', 'userInfo', 'mySocket']),

    },
    methods: {
      //移动棋子
      moveChese(event) {
        //判断超时和轮到我方没
        if (!this.turnMe) {
          return
        }

        //首先判断有没有该位置棋子是否存在
        let img = event.currentTarget;
        let imgId = img.id
        let ids = imgId.split(':');
        let x = ids[0]
        let y = ids[1]
        console.log('上次点击' + this.touchCode + this.codeIndex + '本次点击的棋子' + x + y)
        let code = this.cheses[x][y]
        //棋子不存在
        if (code === 0) {
          //且手中没拿起棋子，本次移动失败
          if (!this.touchCode) {
            return
          } else {
            //移动成功向后台发送消息验证
            console.log('从' + this.codeIndex + '移动到' + x + ',' + y)
            let preX = this.codeIndex[0]
            let preY = this.codeIndex[1]
            /*测试前端界面时用的
            this.$set(this.cheses[x], y, this.cheses[preX][preY])
             this.$set(this.cheses[preX], preY, 0)*/
            this.touchCode = false
            this.codeIndex = []
            //封装棋子移动消息
            let cheseIndex = {
              'messageCode': 102,
              'codeIndex': {
                'code': this.cheses[preX][preY],
                'startX': preX,
                'startY': preY,
                'endX': x,
                'endY': y,
              }
            }
            this.sendMessage(cheseIndex)
            return
          }
        }
        //棋子存在
        if (code !== 0) {
          //若手中已经拿起棋子则判断该位置棋子是否是己方
          if (this.touchCode) {
            if (this.codeIndex[0] === x && this.codeIndex[1] === y) {
              //若两次点击棋子相同取消选中棋子颜色恢复
              console.log('取消选中')
              document.getElementById(imgId).style.cssText = 'opacity:1;width:65px'
              this.touchCode = false
              return
            }
            //如果拿起的棋子是自己的将棋子颜色便但
            if ((code > 0 && this.isCodeZ) || (code < 0 && !this.isCodeZ)) {
              //拿起棋子
              this.touchCode = true
              this.codeIndex = [x, y]
              //变淡棋子颜色方便标识
              document.getElementById(imgId).style.cssText = 'opacity:0.5;width:65px'
              let preImgId = this.codeIndex[0] + ':' + this.codeIndex[1]
              document.getElementById(preImgId).style.cssText = 'opacity:1;width:65px' //取消选中
              return
            }
            //若是对方棋子则发送消息
            let cheseIndex = this.packageCheseIndex(102, this.codeIndex, x, y)
            this.sendMessage(cheseIndex)
            return
          } else {
            //拿起棋子
            this.touchCode = true
            this.codeIndex = [x, y]
            //变淡棋子颜色方便标识
            document.getElementById(imgId).style.cssText = 'opacity:0.5;width:65px'
            return
          }
        }
      },
      //封装消息
      packageCheseIndex(messageCode, codeIndex, x, y) {
        let preX = codeIndex[0]
        let preY = codeIndex[1]
        return {
          'messageCode': messageCode,
          'codeIndex': {
            'code': this.cheses[preX][preY],
            'startX': preX,
            'startY': preY,
            'endX': x,
            'endY': y,
          }
        }
      },

      //计算棋子位置初始化
      initCheses() {
        /*let w = this.codeSize = this.$refs.qipan.clientWidth / 9
        let h = this.codeSizeheight = this.$refs.qipan.clientHeight / 10
        console.log('宽:' + w + '搞：' + h)*/
        if (this.mySocket == null) {
          return;
        }
        this.userName = this.userInfo.userName
        this.isCodeZ = this.cheseIndex.redUserName === this.userName ? true : false
        this.cheses = this.cheseIndex.map.cheses
        this.startTime = this.cheseIndex.map.startTime
        this.overPlusTime = this.cheseIndex.ramainTime
        this.showOverPlushTime()
        this.showGameState(this.cheseIndex.gameState,this.cheseIndex.turnMe)
        this.showStartGameTime()
        //显示双方信息
        this.showRanks()
      },
      //显示对局双方rank
      async showRanks() {
        let result = await reqGetRank(this.userName)
        this.rank = rankUtil.getRankInfo(result)
        let oppresult = await reqGetRank(this.cheseIndex.oppUserName)
        this.otherRank = rankUtil.getRankInfo(oppresult)
      },
      //计算开具多久了
      showStartGameTime() {
        setInterval(() => {
          this.playIngTime = moment(this.startTime).locale('zh_cn').toNow()
        }, 20)
        console.log(this.playIngTime)
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
      //发送消息
      sendMessage(value) {
        value = JSON.stringify(value)
        console.log('发送' + value)
        this.mySocket.send(value)
      },
      showGameState(gamestate,turnMe){
        if (turnMe === this.userName) {
          this.turnMe = true
          this.gameState = '轮到我方'
        } else {
          this.turnMe = false
          this.gameState = '轮到敌方'
        }
        if(gamestate === 400){
          return
        }
        if (gamestate === 401 && this.isCodeZ) {
          this.gameState = '我方胜利'
        } else if (gamestate === 402 && !this.isCodeZ) {
          this.gameState = '我方胜利'
        } else {
          this.gameState = '失败'
        }
      }

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
        this.showGameState(cheseIndex.gameState,cheseIndex.turnMe)
      }
    },
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  #chess
    background url("./images/chess.jpg") no-repeat
    background-size auto
    height 632px
</style>
