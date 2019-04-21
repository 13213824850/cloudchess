<template>
  <div>
    <div class="row" v-if="this.socket">

      <div class="col-sm-5 col-sm-offset-1">
        <div class="row">
          <div class="col-sm-6">
            <h3>对战列表</h3>
          </div>
          <div class="col-sm-6">
            <span class="btn btn-info  bt-lg" @click="choseType">
            <span v-if="matchTime===0">开始匹配</span>
            <span v-else>已匹配{{matchTime}}秒</span>
            </span>
            <span :class="remove_Match"></span>
          </div>
        </div>
        <!--显示对战列表 -->
        <div class="row">

          <div class="row" >
            <h4>
            <div class="row">
              <table class="table table-hover"><thead>
              <tr>
                <td>#</td>
                <td>对局双方</td>
                <td>类型</td>
                <td>时间</td>
              </tr>
              </thead></table>
            </div>
            <div class="row" style="overflow-y: auto;height: 400px">
                <table class="table table-hover">
                  <!--数据-->
                  <tbody>
                  <tr v-for="(gameList,index) in gameLists" class="active">
                    <td>{{index+1}}</td>
                    <td>{{gameList.redNickName + 'vs' + gameList.blackNickName}}</td>
                    <td>{{getType(gameList.matchType)}}</td>
                    <td>{{formateTime(gameList.created)}}</td>
                    <td><a class="btn btn-info" @click="watchPlay(gameList.checkBoardInfoId)">观战</a></td>
                  </tr>
                  </tbody>
                </table>
            </div>
            </h4>
          </div>

        </div>

      </div>

      <!-- 右侧聊天栏-->
      <div class="col-sm-3 col-sm-offset-1">
        <div class="row">
          <!--导航条显示有哪些好友在聊天-->
          <ul class="nav nav-pills">
            <li role="presentation" :class="{active: messageTag}" @click="DaTingMessage()"><a>大厅</a></li>
            <li v-if="Object.keys(messageUser).length != 0" role="presentation" :class="{active: !messageTag}"
                @click="singleMessageShow()"><a>{{messageUser.nickName}}
              <span class="badge">{{messageSingleCount > 0 ? messageSingleCount : ''}}</span></a></li>

            <li role="presentation">

              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                  <a>更多<span>{{messageUserCount > 0 ? messageUserCount : ''}}</span></a> <span class="caret"></span>
                </button>
                <!--显示聊天栏messageusers中记录-->
                <ul class="dropdown-menu" v-if="messageUsers.length > 0" v-for="msg in messageUsers">
                  <!--显示聊天昵称-->
                  <li @click="showSingleMessageUser(msg)"><a>{{msg.nickName}}<span class="badge"
                                                                                   v-if="msg.newMessageCount > 0">{{msg.newMessageCount}}</span></a>
                  </li>
                </ul>
              </div>
            </li>
          </ul>

          <div id="showMessage" style="overflow-y: auto; height: 400px; background-color: #F7F7F7;word-break:break-all">
            <!--大厅聊天-->
            <div class="row" v-if="messageTag" v-for="m in showAllMessages">
              <span :style="{color: m.nickName === userInfo.nickName ? 'blue' : ''}">{{m.nickName + ':  ' + m.message}}</span>

            </div>
            <!--其他聊天-->
            <div v-if="!messageTag " v-for="msg in messageUser.msg">
              <span v-if="msg.message.length > 0">{{msg.nickName + ':   ' + msg.message}}</span>
            </div>
          </div>
          <br>
          <div>
            消息：<input v-model="message" type="text"/>
            <button class="btn-info" @click="sendToAll">发送</button>
          </div>
        </div>
      </div>

      <!--好友-->
      <div class="col-sm-2">
        <div class="row">
          <ul class="nav nav-pills">
            <li role="presentation" :class="{active: showFriendList} " @click="showFrien()"><a>好友栏</a></li>
            <li role="presentation" :class="{active: !showFriendList} " @click="showLaunchMessage()"><a
              style="float: left">申请列表 <span v-if="friendLaunchCount > 0" class="badge">
              {{friendLaunchCount}}</span></a></li>
          </ul>
        </div>


        <!--好友搜索-->
        <div class="row" v-if="showFriendList">
          <div class="row" style="margin-left: 2%">
            <input type="text" v-model="searchNickName"/>
            <span class="glyphicon glyphicon-search" @click="searchUser()"></span>
          </div>
          <br>

          <!--好友显示-->
          <div v-if="searchNickName.length === 0" class="list-group">
            <div v-if="friends.length != 0" v-for="(friend, index) in friends ">
              <a class="list-group-item" :style="{backgroundColor: friend.onLine === 0 ? '#A2A2A2' : '#FFFFFF'}"
                 @contextmenu.prevent="rightShowMeun($event, friend)">
                <span class="glyphicon glyphicon-user"
                      :style="{color: friend.friendSex === 0 ? 'black' : 'red'}"></span>
                <span>{{friend.friendNickName}}</span>
                <div style="float: right">
                  <span>{{showOnLine(friend.onLine)}}</span>
                </div>
              </a>
            </div>
            <div v-if="friends.length === 0">
              &nbsp;&nbsp;<span>战无好友</span>
            </div>
          </div>

          <!--显示查询结果-->
          <div v-if="searchNickName.length > 0">
            <div v-if="searchShowUser != null">
              <a class="list-group-item">
                <span class="glyphicon glyphicon-user"
                      :style="{color: searchShowUser.sex === 0 ? 'black' : 'red'}"></span>
                <span>{{searchShowUser.nickName}}</span>
                <span class="glyphicon glyphicon-plus" style="float: right" @click="addLaunchFriendMessage()"> </span>
              </a>
            </div>
            <div v-else>
              <span>未查到</span>
            </div>
          </div>
        </div>

        <!--显示好友请求-->
        <div v-if="!showFriendList" class="row">
          <!--好友显示-->
          <h4>
            <div class="list-group">
              <div v-for="fl in friendLaunch ">
                <span class="glyphicon glyphicon-user"
                      :style="{color: fl.launchSex === 0 ? 'black' : 'red'}"></span>
                <span>{{fl.launchNickName}}</span>
                <div style="float: right">
                  <span class="glyphicon glyphicon-ok" style="" @click="updateLaunchMessage(fl.id,1)"></span>
                  <span style="visibility:hidden;float: right">a</span>
                  <span class="glyphicon glyphicon-remove" style=" " @click="updateLaunchMessage(fl.id,2)"></span>
                </div>
              </div>
              <div v-if="friendLaunch === null">
                &nbsp;&nbsp;<span>战无消息</span>
              </div>
            </div>
          </h4>
        </div>

      </div>

    </div>


    <div class="row" v-else>
      <h2><a style="margin-top: 200px; align-content: center" :onclick="againConnect">与服务器断开连接，请重试</a>
      </h2>

    </div>


    <!--菜单栏-->
    <context-menu class="right-menu"
                  :target="contextMenuTarget"
                  :show="contextMenuVisible"
                  @update:show="(show) => contextMenuVisible = show">
      <a class="list-group">
        <a class="list-group-item" @click="addMessageUsers()">私聊</a>
        <a class="list-group-item" @click="launchFriendMatch()">对战</a>
        <a class="list-group-item" @click="deleteFriend()">删除</a>
      </a>
    </context-menu>

    <AlertTip :alertText="alertText" v-show="alertShow" @closeTip="closeTip"/>
    <!--模态框-->
    <div class="modal fade " tabindex="-1" role="dialog" id="choseTypeModal">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" style="text-align: center">选择对局</h4>
          </div>
          <div class="modal-body">
            <img src="./images/pipei.png" @click="starMatch(101)"/>
            <img src="./images/haoyou.png" @click="starMatch(104)"/>
            <img src="./images/paiwei.png" @click="starMatch(103)"/>
          </div>
        </div>
      </div>
    </div>

  </div>

</template>

<script type="text/ecmascript-6">
  import {
    reqWs, reqConfirmMatch, reqRefuseGame, reqFriendAgree, reqLaunchMatch, reqFriendRefuse,
    reqGetLaunchCount, reqGeLaunchMessages, reqUserInfoByNickName, reqAddMessage, requpdateMessage,
    reqgetFriends, reqDeleteFriend, reqGameList,
  } from "../../api";
  import {mapActions, mapState} from 'vuex'
  import moment from 'moment'
  import AlertTip from '../../components/AlterTip/AlterTip'
  export default {
    data() {
      return {
        alertShow:false,
        alertText: '',
        gameLists: [], //对战列表显示
        path: reqWs,
        message: '',
        matchTime: 0,
        cheseIndex: '',
        socket: null,
        remove_Match: this.matchTime > 0 ? 'glyphicon glyphicon-remove' : '',
        friends: [],
        intervalId: null,
        showAllMessages: [],
        searchNickName: '',//搜索好友昵称,
        searchShowUser: null,//显示搜索结果
        friendMessageCount: 0,//好友发送的消息
        friendLaunchCount: 0,//好友请求消息
        showFriendList: true,//显示好友了列表 false显示好友请求
        friendLaunch: [],//好友请求数据
        contextMenuTarget: null, //右键菜单点击的地方
        contextMenuVisible: false,//是否显示菜单
        friend: null,//单个好友信息
        messageUsers: [],//聊天列表
        messageUserCount: 0, //聊天消息的个数
        messageSingleCount: 0, //当前窗口未激活消息未读个数
        messageUser: {},//怎在聊天的对象
        messageTag: true, //显示怎在聊天的那个
      }
    },
    mounted() {
      // 初始化
      this.init()
    },
    components:{
      AlertTip
    },
    computed: {
      ...mapState(['userInfo']),
      //对好友排序
    },
    /* components: {
       'vue-context-menu': VueContextMenu,
     },*/
    methods: {

      ...mapActions(['updateCheseIndex', 'recordMySocket']),
      //时间格式化
      formateTime(time) {
        moment(time).locale('zh_cn').toNow()
      },
      //关闭弹框
     async closeTip(){
        this.alertShow = false
        this.alertText = ''
        const result = await reqConfirmMatch()
        if(result.code !== 200){
          this.alertShow = true
          this.alertText = result.message
        }
      },
      //开始匹配
      choseType() {
        if (this.time > 0) {
          return
        }
        $('#choseTypeModal').modal('show')
      },
      starMatch(matchType) {
        //如果选择好友约战则列出可供选择的好友
        if (matchType === 104) {
          alert('请点击好友列表中邀请')
        } else {
          //发送匹配消息
          let msg = this.cheseIndexUpdate(matchType)
          this.send(msg)
        }
        $('#choseTypeModal').modal('hide')
        //计时
        this.intervalId = setInterval(() => {
          this.matchTime++
        }, 1000)
      },
      //封装数据
      cheseIndexUpdate(messageCode) {
        return {
          'messageCode': messageCode
        }
      },

      init: function () {
        if (typeof (WebSocket) === "undefined") {
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
          //开始加载一些初始化数据
          this.intData()
        }
      },
      open: function () {

        this.recordMySocket(this.socket)
        console.log("socket连接成功" + this.socket)
      },
      error: function () {
        this.recordMySocket(null)
        this.socket = null
        console.log("连接错误")
      },
      againConnect() {
        this.socket = new WebSocket(this.path)
      },
      //初始化页面数据
      async intData() {
        //查询好友列表申请个数
        const result = await reqGetLaunchCount()
        //查询对战列表
        const gameListResult = await reqGameList(0)
        if (gameListResult.code === 200) {
          this.gameLists = gameListResult.data.gameLists
        }
        if (result.code === 200) {
          this.friendLaunchCount = result.data.count
        }
      },
      goPlay: function () {
        this.$router.replace("/play")
      },
      getMessage: function (msg) {
        console.log("接受消息", msg.data)
        let cheseIndex = msg.data;
        //转json
        cheseIndex = JSON.parse(cheseIndex);
        let code = cheseIndex.messageCode
        if (code == 200) {
          //更新接受的数据
          this.updateCheseIndex(cheseIndex)
          clearInterval(this.intervalId)
          //去对局页面
          this.goPlay()
          return
        }
        if (code === 203) {
          return
        }
        //匹配成功
        if (code === 204) {
          this.matchSuccess(cheseIndex.ramainTime)
          return
        }
        //显示好友信息
        if (code === 503) {
          this.friends = cheseIndex.map.friends
          return
        }
        //删除好友
        if (code === 506) {
          this.deleteFriendFromServer(cheseIndex.userName)
          return
        }
        //取消匹配对局成功
        if (code === 107) {
          clearInterval(this.intervalId)
          this.matchTime = 0
          return
        }
        //托管棋子移动成功
        if (code === 102) {
          this.updateCheseIndex(cheseIndex)
          return
        }
        //棋子移动成功
        if (code === 108) {
          this.updateCheseIndex(cheseIndex)
          return
        }
        //好友约战消息
        if (code === 104) {
          this.showFriendLaunch(cheseIndex);
          return
        }
        //大厅消息
        if (code === 600) {
          this.addToAllMessage(cheseIndex)
          return
        }
        if (code === 601) {
          this.addSingleMessage(cheseIndex)
          return
        }
        //显示好友添加消息
        if (code === 602) {
          this.friendLaunchCount++
          return
        }
        //请i去观战成功
        if(code === 603){
          this.updateCheseIndex(cheseIndex)
          this.$router.push('/watchPlay')
        }
        //添加gameList
        if(code === 604){
          this.gameLists.add(cheseIndex.map.gameList)
          return
        }
        //删除
        if(code === 605){
          let index = this.gameLists.findIndex(g, g.checkBoardInfoId)
          this.gameLists.splice(index,1)
        }
        if(code === 607){
          this.updateCheseIndex(cheseIndex)
        }
        //好友更新
        if (code === 505) {
          this.updateFriendsFromOnLine(cheseIndex.map.friend, cheseIndex.map.onLine)
          return
        }
        //好友栏更新
        if (code === 507) {
          this.friends.push(cheseIndex.map.friend)
          return
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
          state = '在线'
        } else if (onLine === 2) {
          state = '组队'
        } else if (onLine === 3) {
          state - '游戏中'
        }
        return state
      },
      //查询用户
      async searchUser() {
        let result = await reqUserInfoByNickName(this.searchNickName)
        if (result.code === 200) {
          this.searchShowUser = result.data.userInfo
        }
      },
      showFrien() {
        this.showFriendList = true
      },
      //添加好友请求
      async addLaunchFriendMessage() {
        if (this.searchShowUser == null) {
          return
        }
        let result = await reqAddMessage(this.searchShowUser.userName)
        if (result.code === 200) {
          alert("好友添加请求发送成功")
        }
      },

      //显示好友请求
      async showLaunchMessage() {
        this.showFriendList = false
        //查询未处理的
        const result = await reqGeLaunchMessages(0)
        if (result.code === 200) {
          this.friendLaunch = result.data.friendLaunchMessages
        } else {
          alert(result.message)
        }
      },

      //处理 1同意添加 2拒绝添加
      async updateLaunchMessage(id, state) {
        const result = await requpdateMessage(id, state)
        if (result.code === 200) {
          this.friendLaunch = null
          this.friendLaunchCount--
        } else {
          alert(result.message)
        }
      },

      //匹配成功对局
      matchSuccess(time) {
        //匹配成功开始对局
        this.alertShow = true
        let id = setInterval(() => {
          time--
          this.alertText = '剩余'+time+'秒'
          if(time === 0){
            this.alertText=''
            this.alertShow = false
            clearInterval(id)

          }
        }, 1000)
        /*this.alertText = ''
        if (!confirm('确定开始对局')) {
          //取消发送请求
          const result = reqRefuseGame()
          if (result.code == 200) {
            //清楚计时器
            clearInterval(this.intervalId)
            this.matchTime = 0;
            return
          }
        } else {
          const result = reqConfirmMatch()
          if (result.code == 200) {
            //清楚计时器
            clearInterval(this.intervalId)
            this.matchTime = 0;
            //等待开局
            return
          }
        }*/


      },
      //选择好友对战

      //右键菜单
      rightShowMeun(event, friend) {
        this.contextMenuTarget = event.currentTarget
        this.contextMenuVisible = true
        this.friend = friend
      },

      //删除好友
      async deleteFriend() {
        this.contextMenuVisible = false
        if (this.friend == null) {
          return
        }
        let friendName = this.friend.friendName
        const result = await reqDeleteFriend(friendName)
        if (result.code !== 200) {
          alert(result.msg)
        }
        //更新好友栏
        this.deleteFriendFromServer(friendName)
      },
      async launchFriendMatch() {
        this.contextMenuVisible = false
        if (this.friend === null) {
          alert('获取好友信息失败')
          return
        }
        if (this.friend.onLine != 1) {
          alert('该玩家暂时无法接受邀请')
          return
        }
        let result = await reqLaunchMatch(this.friend.friendName)
        if (result.code != 200) {
          alert(result.message)
          return
        }

      },
      //向单个人私聊
      addMessageUsers() {
        if (this.friend === null) {
          alert('发生故障，请重试')
          return
        }
        //加入聊天列表
        let msg = {
          'newMessageCount': 0,//新消息的个数
          'userName': this.friend.friendName,//聊天的对象
          'nickName': this.friend.friendNickName, //聊天对象的昵称
          'msg': [{
            "nickName": this.friend.friendNickName,
            'userName': this.friend.friendName,
            'message': '',
          }]
        }
        this.messageUsers.push(msg)
        this.messageUser = msg
        //优先显示
        this.messageTag = false
        //隐藏菜单
        this.contextMenuVisible = false
      },
      //显示好友约战确认框
      async showFriendLaunch(cheseIndex) {
        let result
        //超时时间
        let time = cheseIndex.ramainTime
        if (confirm('好友 ' + cheseIndex.oppUserName + ' 邀请你对局游戏')) {
          result = await reqFriendAgree()
        } else {
          result = await reqFriendRefuse()
        }
        if (result.code != 200) {
          console.log("confirm fail: " + result)
          alert(result.message)
        }
        return
      },
      /*
          消息模块方法
       */
      //向所有人发送消息 如果messageTag为false则代表发送单人
      sendToAll() {
        //判断字数是否超出限制
        if (this.message.length >= 100) {
          return;
        }
        let msg = {
          'messageCode': this.messageTag ? 600 : 601,
          'message': this.message,
          'nickName': this.userInfo.nickName,
          'oppUserName': Object.keys(this.messageUser).length === 0 ? '' : this.messageUser.msg[0].userName,
        }
        this.send(msg)
        this.message = ''
      },
      //大厅显示
      addToAllMessage(chessIndex) {
        let msg = {
          'message': chessIndex.message,
          'nickName': chessIndex.nickName
        }
        this.showAllMessages.push(msg)
      },
      //单个显示
      addSingleMessage(cheseIndex) {
        let message = cheseIndex.message
        //向messageuser里面msg添加消息
        let msg = {
          'nickName': cheseIndex.nickName,
          'userName': cheseIndex.userName,
          'message': message
        }

        //遍历是否是当前栏
        if (!this.messageTag) {
          //个人发送成功显示
          if (this.messageUser.userName === cheseIndex.oppUserName) {

            this.messageUser.msg.push(msg)
            //地址引用所以messageusers里面已经更新了
            //this.messageUsers.splice(index,this.messageUser)
            return
          }

          //接受方
          //如果当前栏刚好是接受的消息
          if (this.messageUser.userName === cheseIndex.userName) {
            //当前栏激活状态且接受的消息也是当前栏
            //添加消息
            this.messageUser.msg.push(msg)
            return
          }

          //如果当前栏不是
          if (this.messageUser.userName !== cheseIndex.userName) {
            //不是当前栏
            this.messageUserCount++
            let index = this.getMessageUsersIndex(cheseIndex)
            if (index !== -1) {
              this.messageUsers[index].msg.push(msg)
              this.messageUsers[index].newMessageCount++
              /*let msg = this.messageUsers[index]
              msg.message.push(cheseIndex.message)
              msg.message.newMessageCount++
              this.messageUsers.splice(index, msg)*/
            }
            return
          }
          return
        }

        //判断当前窗口在大厅栏
        if (this.messageTag) {
          //窗口已存在
          if (Object.keys(this.messageUser).length > 0 && this.messageUser.userName === cheseIndex.userName) {
            //当前栏激活状态且接受的消息也是当前栏
            //添加消息
            this.messageUser.message.push(msg)
            this.messageUser.newMessageCount++
            this.messageSingleCount++
            return
          }
          //窗口不是当前对话栏
          if (Object.keys(this.messageUser).length > 0 && this.messageUser.userName !== cheseIndex.userName) {
            this.messageUserCount++
            let index = this.getMessageUsersIndex(cheseIndex)
            if (index !== -1) {
              this.messageUsers[index].msg.push(msg)
            }
            return
          }

          //没有窗口
          if (Object.keys(this.messageUser).length === 0) {
            this.messageUserCount++
            let index = this.getMessageUsersIndex(cheseIndex)
            if (index !== -1) {
              this.messageUsers[index].msg.push(msg)
            }
            return
          }
          return
        }
        return

      },
      //点击聊天列表选中当前聊天栏
      showSingleMessageUser(msg) {
        //重置聊天消息个数
        this.messageUserCount -= msg.newMessageCount
        this.messageSingleCount = 0
        msg.newMessageCount = 0
        this.messageUser = msg
        this.singleMessageShow()
      },
      //获取messageCount下标
      getMessageUsersIndex(cheseIndex) {
        //找到对应的messageUsers下标
        for (let i = 0; i < this.messageUsers.length; i++) {
          if (this.messageUsers[i].userName === cheseIndex.oppUserName) {
            //替换
            return i
          }
        }
        //没发现则新增
        let msg = {
          'newMessageCount': 1,//新消息的个数
          'userName': cheseIndex.userName,
          'nickName': cheseIndex.nickName,
          'msg': [{
            "nickName": cheseIndex.nickName,
            'userName': cheseIndex.userName,
            'message': [cheseIndex.message],
          }]
        }
        this.messageUsers.push(msg)

        return -1
      },


      //返回大厅聊天
      DaTingMessage() {
        this.messageTag = true
      },

      //显示单个聊天记录
      singleMessageShow() {
        this.messageTag = false
      },


      //在线更新好友信息
      updateFriendsFromOnLine(friend, onLine) {
        //更新data中 friends
        for (let i = 0; i < this.friends.length; i++) {
          let f = this.friends[i]
          if (f.friendName == friend) {
            console.log(friend + onLine)
            this.$set(this.friends[i], 'onLine', onLine)
            return
          }
        }
      },
      //删除好友friends渲染页面从服务器接受消息
      deleteFriendFromServer(friendName) {
        for (let i = 0; i < this.friends.length; i++) {
          if (this.friends[i].friendName === friendName) {
            //删除
            this.friends.splice(i, 1)
            return
          }
        }
        return
      },
      //判断类型
      getType(type) {
        if (type === 101) {
          return '匹配对局'
        } else if (type === 103) {
          return '排位赛'
        } else if (type === 104) {
          return '好友对战'
        }
        return '未知'
      },

      //观战
      watchPlay(checkBoardInfoId) {
        //发送请求观战消息
        let msg = {
          'messageCode': 603, //观战
          'checkBoardInfoId': checkBoardInfoId
        }
        this.send(msg)
      }


    },

    //vue销毁
    destroyed() {
      // 销毁监听
      this.socket.onclose = this.close
    }
  }
</script>

<style>
  .right-menu {
    border: 1px solid #eee;
    box-shadow: 0 0.5em 1em 0 rgba(0, 0, 0, .1);
    border-radius: 1px;
    display: block;
    font-family: Microsoft Yahei, Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    position: fixed;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, .2);
    border-radius: 3px;
    z-index: 999;
    display: none;

  a {
    padding: 2px 15px;

  / / width: 120 px;
    height: 28px;
    line-height: 28px;
    text-align: center;
    display: block;
    color: #1a1a1a;

  }

  user agent stylesheet
  a:-webkit-any-link {
    color: -webkit-link;
    cursor: pointer;
    text-decoration: underline;
  }

  a:hover {
  / / background: #42b983;
    background: $ color-primary;
    color: #fff;
  }

  }

</style>
