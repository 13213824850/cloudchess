import Vue from 'vue'
import Router from 'vue-router'
import Login from '../pages/Login/Login.vue'
import Register from '../pages/Login/Register'
import Index from '../pages/Index/Index'
import Play from '../pages/Play/Play'
import Test from '../pages/Test'
import FriendList from '../pages/Index/Friend/FriendList'
import LaunchFriend from '../pages/Index/Friend/LaunchFriend'
import User from '../pages/User/User'
import UserInfo from '../pages/User/UserInfo/UserInfo'
import GameHelp from '../pages/User/GameHelp/GameHelp'
import Feedback from '../pages/User/Feedback/FeedBack'
import HistoryPlay from '../pages/User/HistoryPlay/HistoryPlay'
import WatchPlay from '../pages/WatchPlay/WatchPlay'
import UserAc from '../pages/Login/UserAc'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login,
      meta: {
        showFooter: true,
        showHead: true
      }
    },
    {
      path: '/login',
      component: Login,
      meta: {
        showFooter: true,
        showHead: true
      }
    },
    {
      path: '/userAc',
      component: UserAc,
      meta: {
        showFooter: false,
        showHead: false
      }
    },
    {
      path: '/user',
      component: User,
      meta: {
        showFooter: false,
        showHead: false
      },
      children: [
        {
          path: '',
          redirect: '/user/userInfo',
        },
        {
          path: '/user/userInfo',
          component: UserInfo,
        },
        {
          path: '/user/historyPlay',
          component: HistoryPlay,
        },
        {
          path: '/user/gameHelp',
          component: GameHelp,
        },
        {
          path: '/user/feedback',
          component: Feedback,
        },
      ]
    },
    {
      path: '/register',
      component: Register,
      meta: {
        showFooter: true,
        showHead: true
      }
    },
    {
      path: '/index',
      component: Index,
      meta: {
        showFooter: true,
        showHead: true
      },
      children: [
        {
          path: '/index/friendList',
          component: FriendList
        },
        {
          path: '/index/launcFriend',
          component: LaunchFriend
        },
      ]

    },
    {
      path: '/play',
      component: Play,
      meta:
        {
          showFooter: false,
          showHead:
            false
        }
    },
    {
      path: '/watchPlay',
      component: WatchPlay,
      meta:
        {
          showFooter: false,
          showHead:
            false
        }
    },
    {
      path: '/test',
      component:
      Test
    }
    ,

  ]
})
