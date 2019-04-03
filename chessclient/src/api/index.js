import ajax from './ajax'

//登录
export const reqLogin = (userName, password) => ajax('/auth/accredit', {userName, password},'POST')
//注册
export const reqRegister = (userName,nickName,password) => ajax('/user/register',
  {userName, nickName ,password},'POST')
export const reqUserInfo = ()=> ajax('/user/getUserInfo')
//注销
export const reqLogOut = () => ajax('/auth/logout')
export const reqWs = 'ws://localhost:10010/play/index/'
export const reqPlayWs = 'ws://localhost:10010/play/start/'
//同意对局
export const reqConfirmMatch = () => ajax('/matchGame/confirmMatch')
//取消对局
export const reqRefuseGame = () => ajax('/matchGame/refuseGame')
