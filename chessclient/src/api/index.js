import ajax from './ajax'

//登录
export const reqLogin = (userName, password) => ajax('/auth/accredit', {userName, password},'POST')
//注册
export const reqRegister = ({userName,nickName,password}) => ajax('/user/register',
  {userName, nickName ,password},'POST')
export const reqUserInfo = ()=> ajax('/user/getUserInfo')

//根据用户昵称查询
export const reqUserInfoByNickName = (nickName) => ajax('/user/getUserInfoByNickNAme/'+nickName)

//发送好友添加请求
export const reqAddMessage = (friendUserName) => ajax('/user/addMessage/' + friendUserName,{},'POST')

//查询请求列表

export  const reqGeLaunchMessages =  (state) => ajax('/user/geLaunchMessages/'+state)

//查询请求数量
export const reqGetLaunchCount = () => ajax('/user/getLaunchCount')

//处理请求1同意 2拒绝
export const requpdateMessage = (id, state) => ajax('/user/updateMessage/'+id +'/' + state)

//查询好友列表
export const reqgetFriends = () =>  ajax('/user/getFriends')

//删除好友
export const reqDeleteFriend = (friendName) => ajax('/user/deleteFriend/'+friendName)

//注销
export const reqLogOut = () => ajax('/auth/logout')
export const reqWs = 'ws://localhost:10010/play/index/'
export const reqPlayWs = 'ws://localhost:10010/play/start/'
//同意对局
export const reqConfirmMatch = () => ajax('/matchGame/confirmMatch')
//取消对局
export const reqRefuseGame = () => ajax('/matchGame/refuseGame')
//查看rank信息
export const reqGetRank = (userName) =>  ajax('/rankhis/getRank/'+userName)
//发起好友 对战
export const reqLaunchMatch = (oppUserName) => ajax('/matchGame/lauchPlay/' + oppUserName)
//好友同意
export const reqFriendAgree = () => ajax('/matchGame/friendAgree')
//好友拒绝
export const reqFriendRefuse = () => ajax('matchGame/friendRefuse')



//历史记录
export const reqGameHistory = (pn) => ajax('/rankhis/getGameRecord/'+pn)

//对局记录
export const reqGameList = (type) => ajax('rankhis/getGameList/'+type)
