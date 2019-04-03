import {
  RECEIVE_USER_INFO,
  CHESE_INDEX,
  MY_SOCKET
} from "./mutations-types";

export default {
  [RECEIVE_USER_INFO](state, {userInfo}) {
    state.userInfo = userInfo
  },
  [CHESE_INDEX](state, {cheseIndex}){
    state.cheseIndex = cheseIndex
  },
  [MY_SOCKET](state,{mySocket}){
    state.mySocket = mySocket
  }
}