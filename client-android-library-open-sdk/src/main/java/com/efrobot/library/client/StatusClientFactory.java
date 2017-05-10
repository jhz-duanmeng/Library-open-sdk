package com.efrobot.library.client;

import android.util.Log;

import com.efrobot.library.conn.IRobotConnect;
import com.efrobot.library.conn.RobotMessage;
import com.efrobot.library.data.TypeFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class StatusClientFactory {
    private final static String TAG = "StatusClientFactory";

    private IRobotConnect manager;
    private Object target;
    private String packageName;


    public void updateRobotConnect(IRobotConnect manager) {
        this.manager = manager;
    }

    public void setProxyObject(Object target){
        this.target = target;
    }

    public void setFrom(String packageName) {
        this.packageName = packageName;
    }

    public Object newInstance(ClassLoader pClassLoader, Class[] interfaces ) {
        return Proxy.newProxyInstance(pClassLoader, interfaces , new InvocationHandler() {
            @Override
            public Object invoke(Object pProxy, Method pMethod, Object[] pArgs) throws Throwable {
                Log.d("StatusClientFactory", "  invoke   " +  pMethod.getName() );

                if (pMethod.getDeclaringClass().equals(Object.class) || hasLocalAnnotations(pMethod)) {
                    return pMethod.invoke(target, pArgs);
                }else {
                    if(manager == null) {
                        Log.e(TAG, "manager is null");
                        return pMethod.invoke(target, pArgs);
                    }else {
                        RobotMessage robotMessage = manager.invokeGetStatusMethod(packageName, target.getClass().getCanonicalName(), pMethod.getName());
                        Object result = TypeFactory.converter(robotMessage);
                        Log.d("TypeFactory", "converter result  " + result);
                        return result;
                    }
                }
            }
        });
    }

    private boolean hasLocalAnnotations(Method pMethod) {
        Annotation[] annotations = pMethod.getAnnotations();
        if(annotations.length == 0) return false;

        for(int i = 0; i < annotations.length; i++) {
            if(annotations[i].annotationType().equals(Local.class)) {
                return true;
            }
        }
        return false;
    }
}
