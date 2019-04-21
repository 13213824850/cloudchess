<template>
    <div class="row" style="margin-left: 2%;margin-right: 2%">

      <div>

        <!-- Nav tabs -->
        <ul class="nav nav-tabs" role="tablist">
          <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">象棋历史</a></li>
          <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">玩法</a></li>
          <li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">更多</a></li>
        </ul>
        <br>
        <!-- Tab panes -->
        <div class="tab-content">

          <!--象棋历史-->
          <div role="tabpanel" class="tab-pane active" id="home">
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中国象棋是一种古老的棋类游戏，大约有两千年的历史。是中华文明非物质文化经典产物，艺术价值泛属于整个人类文明进化史的一个分枝。</p>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中国象棋起源于中国。英国著名学者李约瑟博士在其所著《中国科学文化史》中明确提出，国际象棋也是中国人的创造。他详尽地分析了中国古代游戏——六博与天文、象术、数学的关系，他说：“只有在中国，阴阳理论的盛行促使象棋雏形的产生，带有天文性质的占卜术得以发明，继而发展成带有军事含义的一种游戏。</p>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;象棋一词最早出现于战国时期。《楚辞·招魂》中就对其形制以及玩乐方法作过专门记载：“蓖蔽象棋，有六簿些；分营并进，道相迫些；成枭而牟，呼五白些。”意思是说，用玉石（即蓖）做成的相当于骰子。（即蔽）大小的象棋，每方共有6颗；比赛的方法是分曹并进。（指必须两人或两组对局联赛），相互进攻，逼迫对方于死路；最后是赢者“牟”（指成倍）取胜利。击败了敌兵（春秋时兵制，以5人为伍），而发出了胜利的欢呼</p>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;汉刘向《说苑·善说》亦记载，雍门子周以琴见孟尝君说：“足下千乘之君也……燕则斗象棋而舞郑女。”意即劝孟，不妨也像“燕”国一样，斗象棋观歌舞，逍遥一二。所有这些，说明在战国时代“象棋”已经成为一项经常的活动了。因此象棋产生的时间，当在战国之前</p>
          </div>


          <div role="tabpanel" class="tab-pane" id="profile">
            <h4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;棋盘</h4>

            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中国象棋的棋盘有九条平行的竖线和十条平行的横线相交而组成，其中共有九十个交叉点，棋子就摆在这些交叉点上。中间部分（棋盘的第五，第六两横线之间末画竖线的空白地带）称为 河界 。两端的中间（两端第四条到第六条竖线之间的正方形部位），以斜交叉线构成中文米字形方格的地方，叫作 九宫 （它恰好有九个交叉点）。
              中国象棋基本规则</p>
             <h4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;界河：</h4>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;这是一个和国际象棋不同的地方，及对垒双方的中间有一条河界，通常称其为楚河，也就是说棋子过河才能攻打对方的首领。这些规则都是和中国古战场的一些场景类似，并且逐渐演变而来成为中国象棋的行棋基本规则。
              中国象棋基本规则</p>

            <h4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;九宫：</h4>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;双方的底线中心处，也就是纵向中心线分别向两边外移一条线（第四条到第六条竖线）之间的正方形部位，以斜交叉线构成 米 字方格的地方，叫作 九宫 （它恰好有九个交叉点），知道这个区域，对理解一些走棋规则很有帮助。
              中国象棋基本规则</p>

            <h4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;棋子：</h4>

            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所有的棋子共有三十二个，其中又分为红、黑两组（分别代表对垒的一方），每组共有十六个棋子（为了区别双方的棋子，不仅在颜色中有所区别，而且还会使用同音不同字的棋子），其中又各分为七种棋子，其名称和数目如下：
            </p>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 红棋子：帅一个，车、马、炮、相、士各两个，兵五个。</p>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 黑棋子：将一个，车、马、炮、象、士各两个，卒五个。</p>


            <h4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中国象棋基本规则</h4>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 将（帅）：<br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  虽然名称不同，但它们这都是对垒双方的最高统帅，对垒的目的就是通过运用各自的棋子，想方设法将对方的首领将死，方为己方胜利。这两位棋子的最高统帅，只能在九宫内行走，不得走出九宫外。行走的步法为：左、右横走，上、下竖走都行，但每次只能行走一格。将和帅不准在同一直线上直接对面（中间无棋子），如一方已先占据位置，则另一方必须回避，否则就算输了。
              中国象棋基本规则
            </p>

            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 士（仕）：<br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  每行一步棋，只许沿着 九宫 中的斜线行走一步（方格的对角线），行走方位可进、可退，其最终目的也是为了护卫各自的最高将领（帅、将）。
              中国象棋基本规则  </p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 象（相）：<br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  此棋不能越过 河界走入对方的领地，其走法为：只能斜走（两步），可以使用汉字中的田字形象地表述：田字格的对角线，即俗称象（相）走田字。行走方位可进、可退，但是，当象（相）行走的路线中，及田字中心有棋子时（无论己方或者是对方的棋子），则不允许走过去，俗称：塞象（相）眼。
              中国象棋基本规则 </p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  车（車）： <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  此棋是中国象棋中棋力最强的棋子，每行一步棋可以上、下直线行走（进、退）；左、右横走（中间不隔棋子），且行棋步数不限。
              中国象棋基本规则 </p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 炮（砲）： <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    此棋的行棋规则和车（車）类似，横平、竖直，只要前方没有棋子的地方都能行走。但是，它的吃棋规则很特别，必须跳过一个棋子（无论是己方的还是对方的）去吃掉对方的一个棋子。俗称：隔山打炮。
              中国象棋基本规则 </p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;马（馬）： <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   走棋规则：使用中国的日字来形容马的行走方式比较贴切，俗称：马走日字（斜对角线）。但是，这里有一个行走规则，可以将马走日分解为：先一步直走（或一横）再一步斜走，如果在要去的方向，第一步直行处（或者横行）有别的棋子挡住，则不许走过去（俗称：蹩马腿）。行走范围不限，可以进、也可以退。
              中国象棋基本规则</p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 卒（兵）： <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  在没有过河界前，此棋每走一步棋只许向前直走一步（不能后退）；过了河界之后，每行一步棋可以向前直走，或者横走（左、右）一步，但也是不能后退的。根据此规则，卒（兵）走到对方的底线只能左右横走，俗称：兵走老了！
              中国象棋基本规则 </p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 吃子： <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   无论什么棋子，通常只要根据行棋规则能走到的部位有对方的棋子就能吃掉对方的棋子。
              而唯一列外的是炮的吃棋方法，比较特殊，需要中间隔有旗子（无论是己方的还是对方的棋子）才能吃掉对方的棋子。
              中国象棋基本规则</p>
            <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;将死和困毙： <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   一方的棋子攻击对方的将（帅），并在下一步要把它吃掉，称为照将，或简称将。照将不必声明。<br>
              被照将的一方必须立即应将，即用自己的着法去化解被将的状态（而不能应将不顾，而走其它的棋子）。<br>
              如果被照将而无法应将，就算被将死（一方胜棋）。<br>
              轮到走棋的一方，无子可走，就算被困毙（无棋可走这方为输棋）。<br>
              </p>

          </div>


          <div role="tabpanel" class="tab-pane" id="messages">
            <a href="https://baike.baidu.com/item/%E4%B8%AD%E5%9B%BD%E8%B1%A1%E6%A3%8B/278314?fr=aladdin#3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;想了解更象棋知识请点链接跳转到百度百科</a>
          </div>
        </div>

      </div>

    </div>
</template>

<script>
    export default {
        name: "GameHelp"
    }
</script>

<style scoped>

</style>
