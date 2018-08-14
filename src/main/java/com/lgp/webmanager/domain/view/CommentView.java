package com.lgp.webmanager.domain.view;

public interface CommentView {
    Long getUserId();

    String getUserName();

    String getProfilePicture();

    String getContent();

    Long getCreateTime();

    Long getReplyUserId();
}
