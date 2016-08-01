package com.larry.cloundusb.cloundusb.baseclass;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry on 5/14/2016.
 * <p/>
 * <p/>
 * 扫描到的待发送联系人信息
 */
public class SendContactInfo implements Parcelable {
    String contactname;//联系人的信息
    String ipAddress;//  ip地址信息
    int resourceId;//头像的的资源id
    int type;//设备类型  A为1  W 为 2 I为  3
    boolean wifiiSopen;//wifi是否代开


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isWifiiSopen() {
        return wifiiSopen;
    }

    public void setWifiiSopen(boolean wifiiSopen) {
        this.wifiiSopen = wifiiSopen;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }


    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return contactname;

    }

    public void setName(String name) {
        this.contactname = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


     /*
    *
    * 将hashmap转换为list
    *
    * */

    static public List<SendContactInfo> subHashmap(HashMap<String, SendContactInfo> hashMap) {
        List<SendContactInfo> list = new ArrayList<SendContactInfo>();

        Iterator<Map.Entry<String, SendContactInfo>> iterator = hashMap.entrySet().iterator();
        if (hashMap.size() != 0)
            while (iterator.hasNext()) {
                Map.Entry<String, SendContactInfo> entry = iterator.next();
                SendContactInfo t = entry.getValue();
                list.add(t);
            }

        return list;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactname);
        dest.writeString(ipAddress);
        dest.writeInt(resourceId);

    }

    public final static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            SendContactInfo sendContactInfo = new SendContactInfo();
            sendContactInfo.setName(source.readString());
            sendContactInfo.setIpAddress(source.readString());
            sendContactInfo.setResourceId(source.readInt());
            return sendContactInfo;
        }

        @Override
        public Object[] newArray(int size) {
            return new SendContactInfo[0];
        }
    };


    /*
    * 第二个参数判断是否是get 请求还是post请求
    * 如果是post请求则 开始位置为12  judgetype为真
    *
    * 如果是get请求则初始位置为11      judgetype  为false
    *
    * */
    static public SendContactInfo transformSendContactInfor(String protocol, boolean judgeType) {
        SendContactInfo sendContactInfo = new SendContactInfo();
        int type = 0;

        sendContactInfo.setWifiiSopen((protocol.substring(7, 8).equals("0")) ? false : true);
        if (protocol.substring(11, 12).equals("A"))       //代表为安卓设备
        {
            type = 1;
        } else if (protocol.substring(11, 12).equals("W")) {
            type = 2;
        } else if (protocol.substring(11, 12).equals("I")) {
            type = 3;
        }
        sendContactInfo.setType(type);
        if (judgeType == true) {
            sendContactInfo.setIpAddress(protocol.substring(12, protocol.indexOf("_", 13)));//设备ip

        } else {
            String ip = protocol.substring(11, protocol.indexOf("_", 13));
            sendContactInfo.setIpAddress(protocol.substring(11, protocol.indexOf("_", 12)));//设备ip

        }
        sendContactInfo.setName(protocol.substring(protocol.lastIndexOf("_") + 1));
        return sendContactInfo;

    }
}
