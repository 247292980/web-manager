package com.lgp.webmanager.domain.enums;

/**
 * @AUTHOR lgp
 * @DATE 2018/2/9 15:29
 * @DESCRIPTION 信息状态
 **/
public  enum StatusEnum {
    COLLECT_TYPE_PUBLIC("PUBLIC","公开收藏"),
    COLLECT_TYPE_PRIVATE("PRIVATE","私密收藏"),

    FOLLOW_STATUS_FOLLOW("FOLLOW","关注"),
    FOLLOW_STATUS_UNFOLLOW("UNFOLLOW","取消关注"),

    IS_DELETE_YES("YES","删除"),
    IS_DELETE_NO("NO","没删除"),

    LETTER_TYPE_ORIGINAL("ORIGINAL","正常回复"),
    LETTER_TYPE_REPLY("REPLY","回复他人的回复"),
    ;

    private String value;
    private String desc;

    StatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
