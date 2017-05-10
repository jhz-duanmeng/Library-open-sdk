package com.efrobot.library.data;


import android.util.Log;

import com.efrobot.library.conn.RobotMessage;


public class TypeFactory {

    private static final String TAG = "TypeFactory";

    public static Object converter(RobotMessage message) {
        if (message == null) {
            Log.d(TAG, "converter null");
            return null;
        } else if (void.class.getName().equals(message.getType())) {
            return null;
        } else if (boolean.class.getName().equals(message.getType())) {
            byte[] byteArrayValue = message.getData();
            return byteArrayValue[0] == 1;
        } else if (Boolean.class.getName().equals(message.getType())) {
            byte[] byteArrayValue = message.getData();
            return byteArrayValue[0] == 1;
        } else if (int.class.getName().equals(message.getType())) {
            byte[] byteArrayValue = message.getData();
            return (byteArrayValue[0] & 0xFF) << 24 | (byteArrayValue[1] & 0xFF) << 16 | (byteArrayValue[2] & 0xFF) << 8 | (byteArrayValue[3] & 0xFF);
        } else if (Integer.class.getName().equals(message.getType())) {
            byte[] byteArrayValue = message.getData();
            return (byteArrayValue[0] & 0xFF) << 24 | (byteArrayValue[1] & 0xFF) << 16 | (byteArrayValue[2] & 0xFF) << 8 | (byteArrayValue[3] & 0xFF);
        } else if (String.class.getName().equals(message.getType())) {
            return new String(message.getData());
        } else if ("[B".equals(message.getType())) {
            return message.getData();
        }

        return null;
    }

    public static RobotMessage createRobotMessage(Object value) {
        if (value == null) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(void.class.getName());
            return robotMessage;
        } else if (void.class.equals(value.getClass())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            return robotMessage;
        } else if (boolean.class.equals(value.getClass())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            byte[] data = new byte[1];
            data[0] = (byte) ((Boolean) value ? 1 : 0);
            robotMessage.setData(data);
            return robotMessage;
        } else if (Boolean.class.equals(value.getClass())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            byte[] data = new byte[1];
            data[0] = (byte) ((Boolean) value ? 1 : 0);
            robotMessage.setData(data);
            return robotMessage;
        } else if (int.class.equals(value.getClass())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            byte[] data = new byte[4];
            data[0] = (byte) ((Integer) value >> 24);
            data[1] = (byte) ((Integer) value >> 16);
            data[2] = (byte) ((Integer) value >> 8);
            data[3] = (byte) ((int) (Integer) value);
            robotMessage.setData(data);
            return robotMessage;
        } else if (Integer.class.equals(value.getClass())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            byte[] data = new byte[4];
            data[0] = (byte) ((Integer) value >> 24);
            data[1] = (byte) ((Integer) value >> 16);
            data[2] = (byte) ((Integer) value >> 8);
            data[3] = (byte) ((int) (Integer) value);
            robotMessage.setData(data);
            return robotMessage;
        } else if (String.class.equals(value.getClass())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            robotMessage.setData(((String) value).getBytes());
            return robotMessage;
        } else if ("[B".equals(value.getClass().getName())) {
            RobotMessage robotMessage = new RobotMessage();
            robotMessage.setType(value.getClass().getName());
            robotMessage.setData((byte[]) value);
            return robotMessage;
        }

        return null;
    }
}
