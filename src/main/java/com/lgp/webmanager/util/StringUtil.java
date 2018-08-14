package com.lgp.webmanager.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 *
 * 字符串 工具类
 **/
public class StringUtil {
    private static Pattern GET_AT_USER_PATTERN = Pattern.compile("[0-9]+");
	public static List<String> getAtUser(String str){
		Matcher m = GET_AT_USER_PATTERN.matcher(str);
		List<String> result=new ArrayList<String>();
		while(m.find()){
			if(StringUtils.isNoneBlank(m.group().trim())){
				result.add(m.group().trim());
			}
		}
		return result;
	}
    public static String getMessage(String template, String... keys) {
        int count = 0;
        for (String key : keys) {
            template = template.replace("{" + count++ + "}", key);
        }
        return template;
    }
}
