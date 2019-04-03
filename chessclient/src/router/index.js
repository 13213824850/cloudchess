import Vue from 'vue'
import Router from 'vue-router'
import Login from '../pages/Login/Login.vue'
import Register from  '../pages/Login/Register'
import Index from '../pages/Index/Index'
import Play from '../pages/Play/Play'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
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
      }
    },
    {
      path: '/play',
      component: Play,
      meta: {
        showFooter: false,
        showHead: false
      }
    }
  ]
})
