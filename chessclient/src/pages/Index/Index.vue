<template>
  <div class="row">

    <div class="col-sm-5 col-sm-offset-1">
      <div class="row">
            <span class="btn btn-info  bt-lg" @click="starMatch">
            <span v-if="matchTime===0">开始匹配</span>
            <span v-else>已匹配{{matchTime}}秒</span>

            </span>
            <span :class="remove_Match"></span>
      </div>
      <div class="row">
        <div class="col-sm-5">
          <h3>对战列表</h3>
        </div>
      </div>
    </div>
    <router-link to="/play">play</router-link>
    <!-- 右侧好友栏-->
    <div class="col-sm-4 col-sm-offset-2">
      <div class="row">
        <div class="row"><h3>好友列表</h3></div>
        <div class="row">
          <div class="list-group">
            <div v-for="(friend, index) in sortFriends ">
              <a class="list-group-item" :style="{backgroundColor: friend.onLine === 0 ? '#A2A2A2' : '#FFFFFF'}">
                <span class="glyphicon glyphicon-user" :style="{color: friend.friendSex === 0 ? 'black' : 'red'}"></span>
                <span>{{friend.friendNickName}}</span>
                <span style="float: right">{{showOnLine(friend.onLine)}}</span>
              </a>
            </div>
          </div>
        </div>
        <div class="row"><h4>聊天室</h4>
          <div>
            <textarea></textarea>
          </div>
          <div>
            <input v-model="message" type="text"/>
            <button class="btn-info">发送</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import {reqWs,reqConfirmMatch,reqRefuseGame} from "../../api";
  import {mapActions} from 'vuex'
  export default {
    data() {
      return {
        path: reqWs,
        message: '',
        matchTime: 0,
        cheseIndex: '',
        socket: null,
        remove_Match: this.matchTime > 0 ? 'glyphicon glyphicon-remove' : '' ,
        friends: [],
      }
    },
    mounted() {
      // 初始化
      this.init()
    },
    computed: {

      //对好友排序
      sortFriends() {
        return this.friends.sort((f1, f2) => f2.onLine - f1.onLine)
      }
    },

    methods: {
      ...mapActions(['updateCheseIndex','recordMySocket']),
      //开始匹配
      test(){
        console.log('test')
      },
      starMatch() {
        //计时
        this.intervalId = setInterval(() => {
          this.matchTime++
        }, 1000)
        //发送匹配消息
        let msg = this.cheseIndexUpdate(101)
        this.send(msg)
      },
      //封装数据
      cheseIndexUpdate(messageCode) {
        return {
          'messageCode': messageCode
        }
      },

      init: function () {
        if (typeof(WebSocket) === "undefined") {
          alert("您的浏览器不支持socket")
        } else {
          // 实例化socket
          this.socket = new WebSocket(this.path)
          // 监听socket连接
          this.socket.onopen = this.open
          // 监听socket错误信息
          this.socket.onerror = this.error
          // 监听socket消息
          this.socket.onmessage = this.getMessage
        }
      },
      open: function () {
        this.recordMySocket(this.socket)
        console.log("socket连接成功"+this.socket)
      },
      error: function () {
        console.log("连接错误")
      },
      getMessage: function (msg) {
        console.log("接受消息",msg.data)
        let cheseIndex = msg.data;
        //转json
        cheseIndex = JSON.parse(cheseIndex);
        let code = cheseIndex.messageCode
        if(code == 200){
          //更新接受的数据
          this.updateCheseIndex(cheseIndex)
          clearInterval(this.intervalId)
          //去对局页面
          this.$router.replace("/play")
        } else if (code === 203) {
          //正在匹配中
        } else if (code === 204) {
          //匹配成功
          this.matchSuccess()
        } else if (code === 503) {
          //显示好友信息
          this.friends = cheseIndex.map.friends
        }else if(code === 107){
          //取消匹配对局成功
          clearInterval(this.intervalId)
          this.matchTime = 0
        } else {
          //加入匹配对局失败清楚定时器
          clearInterval(this.intervalId)
          console.log("匹配对局失败")
        }
      },
      send: function (msg) {
        let message = JSON.stringify(msg)
        console.log('send: ' + message)
        this.socket.send(message)
      },
      close: function () {
        console.log("socket已经关闭")
      },
      //判断好友状态
      showOnLine(onLine) {
        let state = ''
        if (onLine === 0) {
          state = '离线'
        } else if (onLine == 1) {
          state = '组队中'
        } else if (onLine === 2) {
          state = '游戏中'
        }
        return state
      },
      //匹配成功对局
      matchSuccess(){
        //匹配成功开始对局
        if(!confirm('确定开始对局')){
          //取消发送请求
          const result = reqRefuseGame()
          if (result.code == 200) {
            //清楚计时器
            clearInterval(this.intervalId)
            this.matchTime = 0;
            return
          }
        }else{
          const result = reqConfirmMatch()
          if(result.code == 200){
            //清楚计时器
            clearInterval(this.intervalId)
            this.matchTime = 0;
            //等待开局
            return
          }
        }


      }
    },

    //vue销毁
    destroyed() {
      // 销毁监听
      this.socket.onclose = this.close
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
</style>
