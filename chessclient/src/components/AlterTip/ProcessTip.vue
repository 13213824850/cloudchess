<template>
  <div class="circleProgress_wrapper">
    <div class="wrapper right">
      <div class="circleProgress rightcircle"></div>
    </div>
    <div class="wrapper left">
      <div class="circleProgress leftcircle"></div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "ProcessTip"
    }
</script>

<style scoped>
  .circleProgress_wrapper{
    width: 48px;
    height: 48px;
    margin: 0px auto;
    position: relative;
    border:1px solid #ddd;
  }

  .wrapper{
    width: 24px;
    height: 48px;
    position: absolute;
    top:0;
    overflow: hidden;
  }
  .right{
    right:0;
  }
  .left{
    left:0;
  }
  .circleProgress{
    width: 44px;
    height: 44px;
    border:2px solid rgb(232, 232, 12);
    border-radius: 50%;
    position: absolute;
    top:0;
    -webkit-transform: rotate(45deg);
  }
  .rightcircle{
    border-top:2px solid green;
    border-right:2px solid green;
    right:0;
    -webkit-animation: circleProgressLoad_right 5s linear infinite;
  }
  .leftcircle{
    border-bottom:2px solid green;
    border-left:2px solid green;
    left:0;
    -webkit-animation: circleProgressLoad_left 5s linear infinite;
  }
  @-webkit-keyframes circleProgressLoad_right{
    0%{
      border-top:2px solid wheat;
      border-right:2px solid wheat;
      -webkit-transform: rotate(45deg);
    }
    50%{
      border-top:2px solid rgb(232, 232, 12);
      border-right:2px solid rgb(232, 232, 12);
      border-left:2px solid rgb(81, 197, 81);
      border-bottom:2px solid rgb(81, 197, 81);
      -webkit-transform: rotate(225deg);
    }
    100%{
      border-left:2px solid green;
      border-bottom:2px solid green;
      -webkit-transform: rotate(225deg);
    }
  }
  @-webkit-keyframes circleProgressLoad_left{
    0%{
      border-bottom:2px solid #ED1A1A;
      border-left:2px solid #ED1A1A;
      -webkit-transform: rotate(45deg);
    }
    50%{
      border-bottom:2px solid rgb(232, 232, 12);
      border-left:2px solid rgb(232, 232, 12);
      border-top:2px solid rgb(81, 197, 81);
      border-right:2px solid rgb(81, 197, 81);
      -webkit-transform: rotate(45deg);
    }
    100%{
      border-top:2px solid green;
      border-right:2px solid green;
      border-bottom:2px solid green;
      border-left:2px solid green;
      -webkit-transform: rotate(225deg);
    }
  }
  ---------------------
  作者：blueskynyl
  来源：CSDN
  原文：https://blog.csdn.net/qq_33358062/article/details/79907786
  版权声明：本文为博主原创文章，转载请附上博文链接！
</style>
