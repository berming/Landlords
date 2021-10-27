package com.bermin.landlords.client.util;

public enum PokerType {
    TYPE_SINGLE, //单牌
    TYPE_AA,     //对子
    TYPE_2JOKER, //王炸
    TYPE_AAA,    //3不带
    TYPE_AAAA,   //炸弹
    TYPE_STRAIGHT, //顺子  straight
    TYPE_AAAB,     //3带1。
    TYPE_AAABB,    //3带2。
    TYPE_3344,     //连队。
    TYPE_333444,     // 飞机。
    TYPE_33344456,   // 飞机带单排.
    TYPE_3334445566, // 飞机带对子.
    TYPE_ERROR       //不能出牌
}



/*
扑克牌音译英文POKER，但在英语中说一副扑克牌是a deck of playing card，而不是a deck ofpoker。可见在英语中的扑克牌并非等同于中文中的扑克牌。
实际上poker只是playingcard(游戏卡)的一种，在国外poker往往都和赌博(gambling)联系在一起，如果想和老外玩gambling的话，下面的东东会很有帮助。

扑克牌的四种花色:heart 红桃
spade 黑桃
club 梅花
diamond 方块

扑克牌的名称：
大小王统称joker，其实小丑才是最大的，可以参考王的男人剧情
从二到十: two,three,four,five,six,seven,eight,nine,ten.
　　J是Jack Q是Queen K是King A是Ace

例如：
红桃2 the two of hearts 或者 hearts two
红桃J the jack of hearts
　　黑桃Q the queen of spades
　　梅花K the king of clubs
　　方块A the ace of diamonds


card case 牌盒
deck 整付牌
pile 牌迭
top 牌顶
bottom 牌底
face up 面朝上
face down 面朝下

fan 开扇
spread 摊牌
cut 切牌
one hand cut 单手切牌
false cut 假切
shuffle 洗牌
Hindu Shuffle 印度洗牌
riffle shuffle 鸽尾式洗牌
False/Blind Shuffle假洗牌

double lifts 双翻
secret addition
secret load

force 迫牌
riffle 拨牌
riffle force 拨牌迫牌
Under the Spread Force 摊牌迫牌
classic force 古典迫牌
cross force 十字迫牌
false count 假数牌
Elmsley count岩士利数牌法会比较恰当,通称艾尔姆支雷
Jordon count 乔登数牌
Hamman Count 哈门数牌
reverse
top change 换顶牌

pass 移牌
classic pass 古典移牌
Hamman pass 海曼移牌
easy pass 简单移牌
center pass 中央移牌

half pass 翻牌
偷翻牌这个技巧很特别，因为Half Pass的英文原名也不太能正确地传意,
但中文也不应该叫做翻牌，因为这也是个隐性动作...
Turnover叫翻牌会比较合适,因为它并非隐性动作
false deal 假发牌
second deal 发第二张
bottom deal 发底牌
center deal 发中间牌

palm 掌中藏牌
top palm 藏顶牌
bottom palm 藏底牌
Gambler's Cop赌徒扣牌
side steal 侧边偷牌
Tenkai palm 天海藏牌
back palm 手背藏牌

Spread Control摊牌控牌
convincing control pass (Convincing Control, Convincing是有说服力的意思,不包含确认的意思)

in jog 挪移
break 布雷克
peek 偷看牌
Peek的正式名称是 Spectator's Peek，专门指当魔术师拨牌时，观众叫停然后看那张牌的动作，又，这里魔术师所做的拨牌动作和平时的有所不同。真正的魔术师偷看牌应该叫Glimpse才对，但是后来发展成一些魔术师的偷看牌也称为Peek.
crimp 偷拗牌
switch 偷换牌
glide滑行技法
color change 变牌
To show a card/ Display a card
展示牌
twisting card 转牌
Twisting 的本意是旋转，在魔术中是指牌由牌面/牌背 向上∕向下，变为牌面/牌背 向下∕向上的魔术效果，
而在手指间令一张牌旋转则为Pirouette
twirl,spin比较合适于形容魔术中转牌的动作

folding 折牌
tearing 撕牌

Selection or Selected Card观众选的牌
double backer 双背牌

大小王 joker
K King Q Queen J Jack 红桃 heart
黑桃 club
card games 打牌
cards 纸牌
pack (of cards), deck 一副牌
suit 一组
joker 百搭
ace A牌
king 国王,K
queen 王后,Q
Jack 王子,J
face cards, court cards 花牌(J,Q,K)
clubs 梅花,三叶草
diamonds 方块,红方,钻石
hearts 红桃,红心
spades 黑桃,剑
trumps 胜
to shuffle 洗牌
to cut 倒牌
to deal 分牌
banker 庄家
hand 手,家
to lead 居首
to lay 下赌
to follow suit 出同花牌
to trump 出王牌
to overtrump 以较大的王牌胜另一张王牌
to win a trick 赢一圈
to pick up, to draw 偷
stake 赌注
to stake 下赌注
to raise 加赌注
to see 下同样赌注
bid 叫牌
to bid 叫牌
to bluff 虚张声势
royal flush 同花大顺
straight flush 同花顺
straight 顺子
four of a kind 四张相同的牌
full house 三张相同和二张相同的牌
three of a kind 三张相同的牌
two pairs 双对子
one pair 一对,对子
to go banco 赌全锅
bank 庄家赌本
martingale 手法,诀窍
poker 扑克牌
baccarat 一种纸牌赌博
shoe 带轮子的小桌
whist 惠斯特(一种四人两组对打的游戏)
bridge 桥牌
rummy 拉米(一种纸游戏)
canasta 用两副牌的一种游戏
old maid 老处女(抽出的一张牌后分牌,然后对对子,类似中国的蹩七和蹩王八)
beggar-my-neighbour 把对方全吃光为胜的一种游戏
patience 打通关,过五关
roulette 轮赌
dice 掷色

 */