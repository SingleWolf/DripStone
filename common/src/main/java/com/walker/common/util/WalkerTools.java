package com.walker.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WalkerTools {

    /**
     * 获取对象的真实物理地址
     * @param o 对象
     * @return 物理地址
     */
    public static long getAddressOfObject(Object o) {
        Object[] array = new Object[]{o};
        long objectAddress = -1;
        try {
            Class cls = Class.forName("sun.misc.Unsafe");
            Field field = cls.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);
            Class unsafeCls = unsafe.getClass();
            Method arrayBaseOffset = unsafeCls.getMethod("arrayBaseOffset", Object.class.getClass());
            int baseOffset = (int) arrayBaseOffset.invoke(unsafe, Object[].class);
            Method size = unsafeCls.getMethod("addressSize");
            int addressSize = (int) size.invoke(unsafe);
            switch (addressSize) {
                case 4:
                    Method getInt = unsafeCls.getMethod("getInt", Object.class, long.class);
                    objectAddress = (int) getInt.invoke(unsafe, array, baseOffset);
                    break;
                case 8:
                    Method getLong = unsafeCls.getMethod("getLong", Object.class, long.class);
                    objectAddress = (long) getLong.invoke(unsafe, array, baseOffset);
                    break;
                default:
                    throw new Error("unsupported address size: " + addressSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectAddress;
    }
}
