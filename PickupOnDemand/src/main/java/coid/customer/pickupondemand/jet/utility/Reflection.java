package coid.customer.pickupondemand.jet.utility;

import android.support.annotation.NonNull;

import java.lang.reflect.Method;

public class Reflection
{
    public static Object invokeMethod(@NonNull Object receiver, String methodName, Object... methodArgs) throws Exception {
        Class<?>[] argsClass = null;
        if (methodArgs != null && methodArgs.length != 0) {
            int length = methodArgs.length;
            argsClass = new Class[length];
            for (int i=0; i<length; i++) {
                argsClass[i] = methodArgs[i].getClass();
            }
        }

        Method method = receiver.getClass().getMethod(methodName, argsClass);
        return method.invoke(receiver, methodArgs);
    }
}
