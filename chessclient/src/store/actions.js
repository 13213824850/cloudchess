import {
  RECEIVE_USER_INFO,
  CHESE_INDEX,
  MY_SOCKET,
  DELETE_USER_INFO
} from './mutations-types'
import {reqUserInfo} from "../api";

export default {
//异步获取用户信息
  async getUserInfo({commit}) {
    const result = await reqUserInfo()
    if (result.code === 200) {
      const userInfo = result.data.userInfo
      commit(RECEIVE_USER_INFO, {userInfo})
    }
  },
  deleteUserInfo({commit}){
    commit(DELETE_USER_INFO)
  },
  recordUserInfo({commit}, userInfo) {
    commit(RECEIVE_USER_INFO, {userInfo})
  },
  updateCheseIndex({commit}, cheseIndex) {
    commit(CHESE_INDEX, {cheseIndex})
  },
  recordMySocket({commit}, mySocket){
    commit(MY_SOCKET,{mySocket})
  },

}
