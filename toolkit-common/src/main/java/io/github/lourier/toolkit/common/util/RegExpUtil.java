package io.github.lourier.toolkit.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 正则表达式工具类
 * 元字符：
 *  锚点
 *      '^': 匹配开始的位置；'$': 匹配结束的位置
 *  量词
 *      '*': >=0次；'+': >= 1次；'?': 0|1次；'{n}': 固定的n次；'{n,}': >=n；'{n,m}': n=<x<=m
 *  范围
 *      'x|y': x or y; '[xyz]' x or y or z; '[^xyz]': not (x or y or z); '[a-z]': a to z
 *  简写字符
 *      '.': 非 '\n','\r' 外所有单个字符
 *      '\d' == '[0-9]'; '\D' == '[^0-9]'
 *      '\s'：匹配任意空白字符，等同于 '[\f\n\r\t\v]'; '\S' == '[^\f\n\r\t\v]'
 *      '\w': 匹配包括下划线的单词字符，等同于 '[A-Za-z0-9_]'; '\W' 同理
 *      '\b'
 *  其他字符
 *      转义字符：'\*', '\\', '\+', '\?', '\.'
 * 量词。贪婪匹配(默认)，尽可能进行最长匹配；非贪婪匹配(量词后加上'?')，尽可能进行最短匹配；独占匹配(量词后加上'+')：匹配失败直接退出不会回溯
 * 捕获。括号在正则中用于捕获，被括起来的子表达式称为一个捕获组，例如：'((\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}))'. group1(第一个左括号)表示整个表达式，group2(第二个)则表示年份
 * 匹配模式
 *  - 不区分大小写(i)
 *  - '.'通配模式(s)。让 '.' 也能匹配换行符(\n)
 *  - 多行匹配模式(x)。'^' 匹配多行的开头
 *  - 注释模式(m)。可以添加注释，增加可读性，注释以 '#' 开头，直到行末
 * 环视
 *  - '(?<=abc)x'。匹配x，且x左侧必须是abc
 *  - '(?<!abc)x'。匹配x，且x左侧必须不是abc
 *  - 'x(?=abc)'。匹配x，且x右侧必须是abc
 *  - 'x(?!abc)'。匹配x，且x右侧必须不是abc
 * @Date: 2023/11/15 10:04
 * @Author: Lourier
 */
public class RegExpUtil {

    public static final String RE_PHONE = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    public static final String RE_EMAIL = "";
    public static final String RE_ID_CARD = "";
    public static final String RE_USER_NAME = "";
    public static final String RE_PASSWD = "";
    public static final String RE_DATE = "(\\d{4})-(\\d{2})-(\\d{2})";
    public static final String RE_DATE_TIME = "(((\\d{4})-(\\d{2})-(\\d{2})) ((\\d{2}):(\\d{2}):(\\d{2})))";

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile(RE_DATE_TIME);
        Matcher matcher = pattern.matcher("2022-12-12 12:00:00");
        System.out.println(matcher.matches());
        System.out.println(matcher.toString());
        System.out.println(matcher.toMatchResult());
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
        System.out.println(matcher.group(6));
    }

}
