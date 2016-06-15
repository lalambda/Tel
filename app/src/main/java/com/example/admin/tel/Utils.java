package com.example.admin.tel;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoyafeng on 16/6/3.
 */
public class Utils {

    /**
     * 搜索简拼
     * @param contact
     * @param text
     * @return
     */
    public static int nameSearch(String[] contact, String text) {
        int length = contact.length;
        for (int i = 0; i < length; i++) {
            int index = contact[i].indexOf(text);
            if (index > -1) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 搜索全拼
     * @param contact
     * @param text
     * @return
     */
    public static int nameSearch(List<Map<String, Integer>> contact, String text) {
        int flag = 0;
        for (Map<String, Integer> map : contact) {
            for (String s:map.keySet()){
                if (s.length()<text.length()){
                    if (text.startsWith(s)){
                        text = text.substring(s.length());
                        flag +=1000;       //判断共匹配了几项 每加一项加1000
                    }
                }else {
                    if (s.startsWith(text)){
                        return contact.indexOf(map)-flag/1000+flag;
                    }
                }
            }
        }
        return -1;
    }

    public static String[] spellToStringArray(String pinyin) {
        return pinyin.split(",");
    }

    /**
     * 将字母转化成键盘上的数字
     * @param alphabet
     * @return
     */
    public static char alphaToNumber(char alphabet) {
        switch (alphabet) {
            case 'a':
            case 'b':
            case 'c':
                return '2';
            case 'd':
            case 'e':
            case 'f':
                return '3';
            case 'g':
            case 'h':
            case 'i':
                return '4';
            case 'j':
            case 'k':
            case 'l':
                return '5';
            case 'm':
            case 'n':
            case 'o':
                return '6';
            case 'p':
            case 'q':
            case 'r':
            case 's':
                return '7';
            case 't':
            case 'u':
            case 'v':
                return '8';
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                return '9';
            default:
                return alphabet;
        }
    }


    public static String converterToFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            // 取首字母
                            pinyinName.append(strs[j].charAt(0));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                    // else {
                    // pinyinName.append(nameChar[i]);
                    // }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 汉字转换位汉语拼音
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static PinYinName converterToSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        StringBuffer pinyinNameHead = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null) {
                        int size = strs.length;
                        for (int s = 0; s < size; s++) {
                            char[] pinyin = strs[s].toCharArray();
                            int pLength = pinyin.length;
                            for (int p = 0; p < pLength; p++) {
                                pinyin[p] = alphaToNumber(pinyin[p]);
                            }
                            strs[s] = String.valueOf(pinyin);
                            Log.e("s", "--" + strs[s]);
                        }
                        for (int j = 0; j < strs.length; j++) {
                            pinyinName.append(strs[j]);
                            pinyinNameHead.append(strs[j].charAt(0));
//                            strs[j].c
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                                pinyinNameHead.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinNameHead.append(alphaToNumber(nameChar[i]));
                pinyinName.append(alphaToNumber(nameChar[i]));
            }
            pinyinName.append(" ");
            pinyinNameHead.append(" ");
        }
        PinYinName pinYinName = new PinYinName();
        pinYinName.setSpellAll(discountTheChinese(pinyinName.toString()));//全拼
        pinYinName.setSpellHead(parseTheChineseByObject(discountTheChinese(pinyinNameHead.toString())));//简拼
        return pinYinName;
    }

    /**
     * 去除多音字重复数据
     *
     * @param theStr
     * @return
     */
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = new ArrayList<>();
        // 用于处理每个字的多音字，去掉重复
        Map<String, Integer> onlyOne;
        String[] firsts = theStr.split(" ");
        // 读出每个汉字的拼音
        for (String str : firsts) {
            onlyOne = new Hashtable<>();
            String[] china = str.split(",");
            // 多音字处理
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(s, 0);
                }
//                else {
//                    onlyOne.remove(s);
//                    count++;
//                    onlyOne.put(s, count);
//                }
            }
            mapList.add(onlyOne);
        }
        return mapList;
    }

    /**
     * 解析并组合拼音，对象合并方案
     *
     * @return
     */
    private static String parseTheChineseByObject(
            List<Map<String, Integer>> list) {
        Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
        // 遍历每一组集合
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
                    String str = s;
                    temp.put(str, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp != null && temp.size() > 0) {
                first = temp;
            }
        }
        String returnStr = "";
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                returnStr += (str + ",");
            }
        }
        if (returnStr.length() > 0) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }

}
