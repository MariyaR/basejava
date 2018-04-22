package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {

    public static void main(String[] args) throws IllegalAccessException {
        Resume r = new Resume("uuid1");
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        // TODO : invoke r.toString via reflection
        Method toStringReflection = null;
        try {
            toStringReflection = r.getClass().getDeclaredMethod("toString");
            String s = (String) toStringReflection.invoke(r);
            System.out.println(s);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
         catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}