import {reqGetRank} from "../api";

export default {


  //获取段位信息
  getRankInfo(result) {
    if (result.code != 200) {
      return {
        'code': result.code
      }
    }
    let rank = result.data.rank
    let rg = rank.rankGrade
    let rgs = rank.rankGradeStage
    let str
    let colorValue
    if (rg === 1) {
      str = '青铜'
      colorValue = '#615980'
    } else if (rg === 2) {
      str = '黄金'
      colorValue = '#835D16'
    } else if (rg === 3) {
      str = '铂金'
      colorValue = '#527B97'
    } else if (rg === 4) {
      str = '钻石'
      colorValue = '#9E9DAF'
    } else {
      str = '大师'
      colorValue = '#8A733F'
    }
    if (rgs === 1) {
      str += 'I'
    } else if (rgs === 2) {
      str += 'I'
    } else if (rgs === 3) {
      str += 'III'
    }
    //获取胜率
    let winRateValue
    let countPlayValue = rank.transportCount + rank.winCount
    if (countPlayValue === 0) {
      winRateValue = 0
    } else {
      winRateValue = (rank.winCount / countPlayValue) + ''
      winRateValue = winRateValue.substring(0, 4) * 100

    }
    let respondMsg = {
      'stage': str,
      'star': rank.star,
      'winRate': winRateValue,
      'countPlay': countPlayValue,
      'nickName': rank.nickName,
      'windCount': rank.winCount,
      'failCount': rank.transportCount,
      'code': 200,
      'colorValue': colorValue
    }
    return respondMsg
  },



}
