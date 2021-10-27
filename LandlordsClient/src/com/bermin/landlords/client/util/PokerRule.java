package com.bermin.landlords.client.util;

import com.bermin.landlords.client.model.PokerLabel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokerRule {
    public static PokerType checkPokerType (List<PokerLabel> list) {

        Collections.sort(list);

        PokerType pokerType = PokerType.TYPE_ERROR;
        int count = list.size();
        switch (count) {
            case 0:
                break;

            case 1: // 单张
                pokerType = PokerType.TYPE_SINGLE;
                break;

            case 2: // 两张
                if(isSame(list, count)){ //对子
                    pokerType = PokerType.TYPE_AA;
                }
                if(isWangZha(list)){ //王炸
                    pokerType = PokerType.TYPE_2JOKER;
                }
                break;

            case 3: // 三张
                if(isSame(list, count)){ // 三个头
                    pokerType = PokerType.TYPE_AAA;
                }
                break;

            case 4: // 四张
                if(isSame(list, count)){ //炸弹
                    pokerType = PokerType.TYPE_AAAA;
                }
                if(isSanDaiYi(list)){ //三带一
                    pokerType = PokerType.TYPE_AAAB;
                }
                break;

            default: // 五张以上
                if(isShunZi(list)){
                    pokerType = PokerType.TYPE_STRAIGHT;   //顺子
                }else if(isSanDaiYiDui(list)){
                    pokerType = PokerType.TYPE_AAABB;  //三带一对
                }else if(isLianDui(list)){
                    pokerType = PokerType.TYPE_3344; //连对
                }else if(isFeiJI(list)){
                    pokerType = PokerType.TYPE_333444; //双飞or双飞双带
                }else if(isFeiJIDaiChiBang1(list)){
                    pokerType = PokerType.TYPE_33344456; //双飞or双飞双带
                }
                else if(isFeiJIDaiChiBang2(list)){
                    pokerType = PokerType.TYPE_3334445566; //双飞or双飞双带
                }
        }

        System.out.println("pokerType:" + pokerType);
        return pokerType;
    }

    public static boolean isWangZha(List<PokerLabel> list){
        if((list.get(0).getNum() == 16 && list.get(1).getNum() == 17)
                ||(list.get(0).getNum() == 17 && list.get(1).getNum() == 16)){
            return true;
        }
        return false;
    }

    /**
     * 判断list内扑克是否相同
     */
    public static boolean isSame(List<PokerLabel> list,int count){
        for(int j=0;j<count-1;j++){
            int a=list.get(j).getNum();
            int b=list.get(j+1).getNum();
            if(a!=b){
                return false;
            }
        }
        return true;
    }

    /**
     *  判断是否是三带一
     */
    public static boolean isSanDaiYi(List<PokerLabel> list){
        List<PokerLabel> temp= new ArrayList<>();
        temp.addAll(list);
        if(isSame(temp, 3)){
            return true;
        }
        temp.remove(0);
        return isSame(temp, 3);
    }

    /**
     *  判断是否是三带一对
     */
    public static boolean isSanDaiYiDui(List<PokerLabel> list){
        List<PokerLabel> temp= new ArrayList<>();
        temp.addAll(list);
        if(temp.size()==5){
            if(isSame(temp, 3)){
                temp.remove(0);
                temp.remove(0);
                temp.remove(0);
                return isSame(temp, 2);
            }
            if(isSame(temp, 2)){
                temp.remove(0);
                temp.remove(0);
                return isSame(temp, 3);
            }
        }
        return false;
    }


    /**
     * 判断是否是顺子
     */
    public static boolean isShunZi(List<PokerLabel> list){
        for(int i=0;i<list.size()-1;i++){
            int a=list.get(i).getNum();
            int b=list.get(i+1).getNum();
            if(b-a!=1){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是连对
     */
    public static boolean isLianDui(List<PokerLabel> list){
        int size=list.size();
        if(size<6&&size%2!=0){
            return false;
        }
        for(int i=0;i<size;i++){
            int a=list.get(i).getNum();
            int b=list.get(i+1).getNum();
            if(a!=b){
                return false;
            }
            i++;
        }
        for(int i=0;i<size;i++){
            int a=list.get(i).getNum();
            int b=list.get(i+2).getNum();
            if(b-a!=1){
                return false;
            }
            i++;
            i++;
        }
        return true;
    }

    /**
     * 判断是不是双飞
     */
    public static boolean isFeiJI(List<PokerLabel> list){
        List<Integer> count=feiCount(list);
        if(count!=null&&count.size()>=2){
            int len=count.size();
            for(int i=0;i<len-1;i++){
                if(count.get(i+1)-count.get(i)!=1){
                    return false;
                }
            }
            int dui=feiDuiCount(list,count);
            if(dui==0){
                return true;
            }
        }
        return false;
    }

    //飞机带翅膀 3311
    public static boolean isFeiJIDaiChiBang2(List<PokerLabel> list){
        List<Integer> feiji=feiCount(list);
        if(feiji!=null&&feiji.size()>=2){
            int len=feiji.size();
            for(int i=0;i<len-1;i++){
                if(feiji.get(i+1)-feiji.get(i)!=1){
                    return false;
                }
            }
            int dui=feiDuiCount(list,feiji);
            return dui == feiji.size();
        }
        return false;
    }

    //飞机带翅膀3322
    public static boolean isFeiJIDaiChiBang1(List<PokerLabel> list){
        List<Integer> feiji=feiCount(list);
        if(feiji!=null&&feiji.size()>=2){
            int len=feiji.size();
            for(int i=0;i<len-1;i++){
                if(feiji.get(i+1)-feiji.get(i)!=1){
                    return false;
                }
            }
            int dui=feiDanZhangCount(list,feiji);
            if(dui==feiji.size()){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断三个头有几个
     */
    public static List<Integer> feiCount(List<PokerLabel> list){
        List<Integer> cnt = new ArrayList<>();
        for(int i=0;i<list.size()-2;i++){
            int a=list.get(i).getNum();
            int b=list.get(i+1).getNum();
            int c=list.get(i+2).getNum();
            if(a==b&&a==c){
                cnt.add(a);
            }
        }
        return cnt;
    }
    /**
     * 判断双飞中对子有几个
     */
    public static int feiDuiCount(List<PokerLabel> list,List<Integer> feiji){
        List<PokerLabel> temp= new ArrayList<>();
        temp.addAll(list);
        int cnt = 0;
        for(int i=0;i<temp.size();i++){
            for(int j=0;j<feiji.size();j++){
                int a=list.get(i).getNum();
                if(a==feiji.get(j)){
                    temp.remove(i);
                }
            }
        }
        int size=temp.size();
        if (size > 0 && size % 2 == 0) {
            for(int i=0;i<temp.size()-1;i++){
                int a=list.get(i).getNum();
                int b=list.get(i+1).getNum();
                if(a==b){
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * 判断双飞中单张有几个
     */
    public static int feiDanZhangCount(List<PokerLabel> list,List<Integer> feiji){
        List<PokerLabel> temp= new ArrayList<>();
        temp.addAll(list);
        int cnt = 0;
        for(int i=0;i<temp.size();i++){
            for(int j=0;j<feiji.size();j++){
                int a=list.get(i).getNum();
                if(a==feiji.get(j)){
                    temp.remove(i);
                }
            }
        }
        int size=temp.size();
        if(size>0){
            for(int i=0;i<temp.size()-1;i++){
                int a=list.get(i).getNum();
                int b=list.get(i+1).getNum();
                if(a!=b){
                    cnt++;
                }
            }
        }
        return cnt;
    }

    //比大小
    public static boolean isBigger(List<PokerLabel> prevList,List<PokerLabel> currentList){
        // 首先判断牌型是不是一样
        PokerType pokerType = checkPokerType(prevList);
        if (pokerType.equals(checkPokerType(currentList))) {
            // 根据牌型来判断大小
            if (PokerType.TYPE_SINGLE.equals(pokerType)) {
                // 单张
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_2JOKER.equals(pokerType)) {
                // 王炸
                return false;
            } else if (PokerType.TYPE_AA.equals(pokerType)) {
                // 对子
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_AAA.equals(pokerType)) {
                // 三张
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_AAAB.equals(pokerType)) {
                // 三带一
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_AAABB.equals(pokerType)) {
                // 三带一对
                return compare(prevList, currentList);
            } else if (PokerType.TYPE_AAAA.equals(pokerType)) {
                // 炸弹
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_STRAIGHT.equals(pokerType)) {
                // 顺子
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_3344.equals(pokerType)) {
                // 连对
                return compareLast(prevList, currentList);
            } else if (PokerType.TYPE_333444.equals(pokerType)) {
                // 双飞
                return compare(prevList, currentList);
            }
            else if (PokerType.TYPE_33344456.equals(pokerType)) {
                // 飞机带翅膀（单张）
                return compare(prevList, currentList);
            }
            else if (PokerType.TYPE_3334445566.equals(pokerType)) {
                // 飞机带翅膀（对子）
                return compare(prevList, currentList);
            }

        }else if(currentList.size()==2){
            //判断是不是王炸
            return isWangZha(currentList);
        } else if(currentList.size()==4){
            //判断是不是炸弹
            return isSame(currentList, 4);
        }
        return false;
    }

    public static boolean compareLast(List<PokerLabel> prevList, List<PokerLabel> currentList) {
        if (prevList.get(prevList.size() - 1).getNum() < currentList.get(currentList.size() - 1).getNum()) {
            return true;
        }
        return false;
    }

    public static boolean compare(List<PokerLabel> prevList, List<PokerLabel> currentList) {
        int a = san(prevList);
        int b = san(currentList);
        if (a == -1 || b ==-1 ){
            return false;
        }
        return b > a;
    }

    public static int san(List<PokerLabel> list){
        for(int i=0;i<list.size()-2;i++){
            int a=list.get(i).getNum();
            int b=list.get(i+1).getNum();
            int c=list.get(i+2).getNum();
            if(a==b&&a==c){
                return a;
            }
        }
        return -1;
    }
}
