package com.lgp.webmanager.domain.view;
/**
 * 脑抽了，这玩意就是当时想拿来试试，只有get方法能不能取到值
 * 觉得用接口收参数比类占得资源更少
 * 其实接口收了参数，还是要传到类实体传出去
 * 真脑抽系列
 * 理解dto，vo
 * */
public interface CollectSummaryView {
    Long getId();

    Long getUserId();

    String getProfilePicture();

    String getTitle();

    String getType();

    String getUrl();

    String getLogoUrl();

    String getRemark();

    String getDescription();

    Long getLastModifyTime();

    Long getCreateTime();

    String getUserName();

    Long getFavoriteId();

    String getFavoriteName();

    String getOperId();
}