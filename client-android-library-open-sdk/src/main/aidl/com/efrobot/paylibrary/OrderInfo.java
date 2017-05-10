package com.efrobot.paylibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangyun on 2016/10/24.
 */

public class OrderInfo implements Parcelable {

    /**
     * 订单总金额（精确到小数点后两位）.
     */
    private String totalAmount;

    /**
     * 商品描述.
     */
    private String body;

    /**
     * 商户唯一订单号.
     */
    private String outTradeNo;

    /**
     * 商品的标题/交易标示/订单标题/订单关键字.
     */
    private String subject;

    /**
     * 开放平台账户ID.
     */
    private String devId;

    /**
     * 开放平台分配给开发者的应用ID.
     */
    private String appId;

    /**
     * 商户签名字符串.
     */
    private String privateKey;

    /**
     * 请求时间戳.
     */
    private String timestamp;

    /**
     * 机器人主动通知商户服务器地址.
     */
    private String notifyUrl;


    /**
     * 销售产品码（签约用户申请销售appId下对应产品码）.
     */
    private String productCode;

    /**
     * 支付生成token.
     */
    private String token;


    public OrderInfo(Parcel in) {
        totalAmount = in.readString();
        body = in.readString();
        outTradeNo = in.readString();
        subject = in.readString();
        devId = in.readString();
        appId = in.readString();
        privateKey = in.readString();
        timestamp = in.readString();
        notifyUrl = in.readString();
        productCode = in.readString();
        token = in.readString();
    }

    public OrderInfo() {

    }

    /**
     * 设置订单总金额（精确到小数点后两位）.
     *
     * @param totalAmount 交易金额 长度9位
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 设置商品描述（对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body）.
     *
     * @param body 商品描述 比如：小胖之声 长度：128
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 设置商户唯一订单号.
     *
     * @param outTradeNo 长度：64
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    /**
     * 设置商品的标题/交易标示/订单标题/订单关键字.
     *
     * @param subject 长度:256
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 设置开放平台账户ID.
     *
     * @param devId 长度:20
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * 设置开放平台分配给开发者的应用ID.
     *
     * @param appId 长度：32
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }



    /**
     * 设置请求时间戳.
     *
     * @param timestamp 格式：2016-11-01 11:31:36
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 设置机器人主动通知商户服务器地址.
     *
     * @param notifyUrl 长度256
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }



    /**
     * 设置销售产品码（签约用户申请销售appId下对应产品码）.
     *
     * @param productCode 长度：10
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * 第三方在开放平台申请私钥
     *
     * @param privateKey 私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getBody() {
        return body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public String getDevId() {
        return devId;
    }

    public String getAppId() {
        return appId;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }



    public String getProductCode() {
        return productCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(totalAmount);
        dest.writeString(body);
        dest.writeString(outTradeNo);
        dest.writeString(subject);
        dest.writeString(devId);
        dest.writeString(appId);
        dest.writeString(privateKey);
        dest.writeString(timestamp);
        dest.writeString(notifyUrl);
        dest.writeString(productCode);
        dest.writeString(token);

    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            OrderInfo orderInfo = new OrderInfo(source);
            return orderInfo;
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[0];
        }
    };

    @Override
    public String toString() {
        return "OrderInfo{" +
                "totalAmount='" + totalAmount + '\'' +
                ", body='" + body + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", subject='" + subject + '\'' +
                ", devId='" + devId + '\'' +
                ", appId='" + appId + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", productCode='" + productCode + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public  boolean checkOrderInfoIegality() {
        if (isEmpty(appId, 32) || isEmpty(this.devId, 20) || isEmpty(this.timestamp, 20) || isEmpty(this.notifyUrl, 256)
                || isEmpty(this.body, 256) || isEmpty(this.subject, 256) || isEmpty(this.outTradeNo, 64) || isEmpty(this.totalAmount, 9)
               || isEmpty(this.productCode, 10)  ) {
            return  false;
        }
        return true;
    }

    public static boolean isEmpty(String str, int length) {
        if (str == null || str.length() == 0 || str.length() > length) {
            return true;
        } else {
            return false;
        }
    }
}
