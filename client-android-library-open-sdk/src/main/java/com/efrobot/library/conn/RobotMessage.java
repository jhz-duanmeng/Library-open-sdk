package com.efrobot.library.conn;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class RobotMessage implements Parcelable {

    private final static String TAG = "RobotMessage";
    /**
     * 数据来源标示
     */
    private String identifier;

    /**
     *  数据索引（对应RobotState的各个Index）
     */
    private int index = -1;

    /**
     * 数据类型
     */
    private String type;

    /**
     * 数据内容
     */
    private byte[] data;

    /**
     * 发送时 时间戳 (System.currentTimeMillis())
     */
    private long sendTimeStamp = 0;

    /**
     * 接受时 时间戳 (System.currentTimeMillis())
     */
    private long receiveTimeStamp = 0;


    public RobotMessage() { }

    protected RobotMessage(Parcel in) {

        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        Log.d(TAG, "readFromParcel");
        index = in.readInt();
        identifier = in.readString();
        type = in.readString();
        data = in.createByteArray();
        sendTimeStamp = in.readLong();
        receiveTimeStamp = in.readLong();
    }


    public static final Creator<RobotMessage> CREATOR = new Creator<RobotMessage>() {
        @Override
        public RobotMessage createFromParcel(Parcel in) {
            return new RobotMessage(in);
        }

        @Override
        public RobotMessage[] newArray(int size) {
            return new RobotMessage[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getSendTimeStamp() {
        return sendTimeStamp;
    }

    public void setSendTimeStamp(long sendTimeStamp) {
        this.sendTimeStamp = sendTimeStamp;
    }

    public long getReceiveTimeStamp() {
        return receiveTimeStamp;
    }

    public void setReceiveTimeStamp(long receiveTimeStamp) {
        this.receiveTimeStamp = receiveTimeStamp;
    }
        @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel");
        dest.writeInt(index);
        dest.writeString(identifier);
        dest.writeString(type);
        dest.writeByteArray(data);
        dest.writeLong(sendTimeStamp);
        dest.writeLong(receiveTimeStamp);
    }
}
