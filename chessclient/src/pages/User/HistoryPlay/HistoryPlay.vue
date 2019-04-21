<template>
  <div class="row" style="margin-left: 2%">
    <div class="row">
      <h4>
        <table class="table table-hover">

          <thead>
          <tr>
            <td>#</td>
            <td>对手</td>
            <td>时间</td>
            <td>结果</td>
            <td>开始时间</td>
          </tr>
          </thead>
          <!--数据-->
          <tbody>
          <tr v-for="(gameHistory,index) in pageInfo.list" class="active">
            <td>{{index+1}}</td>
            <td>{{gameHistory.otherNickName}}</td>
            <td>{{gameHistory.playTime}}</td>
            <td><span
              :style="{color : gameHistory.result == 1 ? '#FF4444' : ''}">{{gameHistory.result == 1 ? '胜利' : '失败'}}</span>
            </td>
            <td>{{formateTime(gameHistory.created)}}</td>
          </tr>
          </tbody>
        </table>
      </h4>
    </div>

    <div class="row">
      <div class="col-sm-6 col-sm-offset-6">
        <!--页码-->
        <nav aria-label="Page navigation">
          <ul class="pagination">
            <li>
              <a aria-label="Previous" v-if="pageInfo.hasPreviousPage">
                <span aria-hidden="true">&laquo;</span>
              </a>
            </li>
            <!--<li>当前第{{pageInfo}}页</li>-->
            <li>
              <a aria-label="Next" v-if="pageInfo.hasNextPage">
                <span aria-hidden="true">&raquo;</span>
              </a>
            </li>
            <li>总共{{pageInfo.total}}条记录</li>
          </ul>
        </nav>
      </div>
    </div>

  </div>
</template>

<script>
  import {reqGameHistory} from '../../../api/index'
  import moment from 'moment'

  export default {
    name: "HistoryPlay",
    data() {
      return {
        pageInfo: {},//返回的历史记录信息
      }
    },
    created() {
      //初始化数据
      this.initData()
    },
    methods: {
      //初始化数据查询第一页历史记录
      async initData() {
        const result = await reqGameHistory(1)
        console.log(result)
        if (result.code !== 200) {
          alert(result.message)
          return
        }
        this.pageInfo = result.data.pageInfo
      },

      //格式化时间
      formateTime(time) {
        return moment(time).locale('zh_cn').format('LLL')
      }

    }
  }
</script>

<style scoped>

</style>
