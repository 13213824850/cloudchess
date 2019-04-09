import {reqGetRank} from "../api";

export default {


  //获取段位信息
  getRankInfo(userName) {
    let msg = reqGetRank(userName)
    console.log(msg.code)
    let rg = rank.rankGrade
    let rgs = rank.rankGradeStage
    let str
    let colorValue
    if (rg === 1) {
      str = '青铜'
      colorValue = ''
    } else if (rg === 2) {
      str = '黄金'
    } else if (rg === 3) {
      str = '铂金'
    } else if (rg === 4) {
      str = '钻石'
    } else {
      str = '大师'
    }
    if (rgs === 1) {
      str += 'I'
    } else if (rgs === 2){
      str += 'I'
    } else if (rgs === 3){
      str += 'III'
    }
    //获取胜率
    let winRateValue
    let countPlayValue = rank.transportCount + rank.winCount
    if (countPlayValue === 0){
      winRateValue = 0
    } else {
      winRateValue = (rank.winCount / countPlayValue )*100
    }

    return {
      stage : str,
      star: rank.star,
      winRate: winRateValue,
      countPlay: countPlayValue,
      userName : rank.userName,
      windCount : rank.winCount,
      failCount : rank.transportCount,
    }
  },


}
