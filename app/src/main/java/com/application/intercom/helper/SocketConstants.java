package com.application.intercom.helper;

public class SocketConstants {
    /*http://3.234.89.113:4000/room_join*/
    public static final String SOCKET_URI = "http://3.234.89.113:4000";//stagging
    public static final String SOCKET_URI2 = "http://3.15.117.169:4000";//production1
    public static final String SOCKET_URI3 = "http://intercomapp.xyz:5065";//production2
    public static final String TEST = "https://jobyoda.org:8080";//production2
    public static final String EVENT_ROOM_JOIN = "joined";
    public static final String EVENT_ROOM_LEAVE = "room_leave";
    public static final String EVENT_MESSAGE = "message";
    public static final String ROOM_ID = "roomId";
    public static final String SENDER_ID = "sender";
    public static final String RECEIVER_ID = "reciever";
    public static final String RECIPE_ID = "recipe_id";
    public static final String TYPE = "type";
    public static final String IMAGE_EXIST = "image_exist";
    public static final String VIDEO_EXIST = "video_exist";
    public static final String FILE_EXIST = "file_exist";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_ID = "message_id";
    public static final String CREATED_AT = "createdAt";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String USER_NAME = "username";
    public static final String MSGTYPE = "msgType";
    public static final String CHATTYPE = "chatType";
    public static final String LAST_SEEN = "last_seen";
    public static final String IS_GROUP = "is_group";
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FAILURE = 400;
    public static final int STATUS_SERVER_ERROR = 500;
    public static final String DATE_FORMAT_DMY = "day_month_year";
    public static final String DATE_FORMAT_YMD = "year_month_day";
    public static final String TIME_FORMAT_24 = "format_24";
    public static final String TIME_FORMAT_12 = "format_12";
    public static final String SEND_PHOTO = "send_photo";

    public static final String SEND_VIDEO = "send_video";
    public static final String VIDEO_PATH = "video_path";
    public static final String IMAGE_PATH = "image_path";

}
