package com.icourt.common;

import com.icourt.core.json.JsonKit;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author june
 */
public class ClassKit {

    public static final String SET = "set";
    public static final String GET = "get";

    private static Logger logger = LoggerFactory.getLogger(ClassKit.class);

    /**
     * 初始化实例
     * @param clazz
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化实例
     * @param clazz
     * @return
     */
    public static Object newInstance(String clazz) {
        try {
            Class<?> loadClass = getDefaultClassLoader().loadClass(clazz);
            return loadClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载类
     * @param clazz
     * @return
     */
    public static Class<?> loadClass(String clazz) {
        try {
            Class<?> loadClass = getDefaultClassLoader().loadClass(clazz);
            return loadClass;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前线程的classLoader
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 返回属性Setter方法名
     * @param property 属性名
     * @return 属性Setter方法名
     */
    public static String getSetterMethodName(String property) {
        if(StrKit.isEmpty(property)){
            return null;
        }
        return SET + StrKit.capitalize(property);
    }

    /**
     * 返回属性Getter方法名
     * @param property 属性名
     * @return 属性Getter方法名
     */
    public static String getGetterMethodName(String property) {
        if(StrKit.isEmpty(property)){
            return null;
        }
        return GET + StrKit.capitalize(property);
    }

    /**
     * 获取is开头的方法名
     * @param property
     * @return
     */
    public static String getIsMethodName(String property) {
        if(StrKit.isEmpty(property)){
            return null;
        }
        return "is" + StrKit.capitalize(property);
    }

    /**
     * 是否存在该字段的get方法
     * @param clz
     * @param property
     * @return
     */
    public static boolean hasGetMethod(Class<?> clz, String property){
        Method method = getMethod(clz,getGetterMethodName(property));
        return method!=null;
    }

    /**
     * 是否存在该字段的set方法
     * @param clz
     * @param property
     * @return
     */
    public static boolean hasSetMethod(Class<?> clz, String property){
        try {
            Field field = getField(clz,property);
            Method setMethod = getMethod(clz,getSetterMethodName(property));
            boolean flg = setMethod.getParameterTypes()[0].getName().equals(field.getType().getName());
            return flg;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置属性值
     * @param object 对象
     * @param property 属性名
     * @param parameterValue 方法参数值
     */
    public static boolean inject(Object object, String property, Object... parameterValue) {
        if(object==null || StrKit.isEmpty(property) || parameterValue==null || parameterValue[0]==null){
            return false;
        }
        Class<?> clz = AopTargetKit.getDoradoProxyTargetClass(object);
        Field field = getField(clz,property);
        if(field!=null){
            try{
                Method setMethod = getMethod(clz,getSetterMethodName(property));
                Assert.notNull(setMethod);
//                boolean flg = setMethod.getParameterTypes()[0].getName().equals(field.getType().getName());
//                if(flg){
//                    setMethod.invoke(object, parameterValue);
//                    return true;
//                }
                setMethod.invoke(object, parameterValue);
                return true;
            }catch (Exception e){
//                logger.warn("对象:"+object.getClass()+" "+property+"字段不存在get或set方法,error:"+e.getMessage());
//                e.printStackTrace();
//                return false;
                throw new RuntimeException(e);
            }
        }
        logger.warn("对象:"+object.getClass()+" 不存在"+property+"字段 或不存在set方法 或有多个set方法");
        return false;
    }

    /**
     * 获取对象的声明的字段的集合
     * @param clz
     * @return
     */
    public static List<String> getFieldList(Class<?> clz){
        if(StrKit.isEmpty(clz)){
            return null;
        }
        String[] strs = getFieldMap(clz).keySet().toArray(new String[0]);
        return Arrays.asList(strs);
    }

    /**
     * 获取对象的声明的所有字段
     * @param clz
     * @return
     */
    public static Map<String, Class<?>> getFieldMap(Class<?> clz){
        if(StrKit.isEmpty(clz)){
            return null;
        }
        List<Field> fieldList = getAllFields(clz,null);

        Map<String, Class<?>> map= new HashMap<String, Class<?>>();

        for (Field field : fieldList){
            String name = field.getName();
            Class<?> clazz = field.getType();

            //没有get set方法的排除

            map.put(name,clazz);
        }
        return map;
    }



    /**
     * 通过实体类的get方法获取属性值
     * @param object
     * @param property
     * @param <T>
     * @return
     */
    public static <T> T getValue(Object object, String property) {
        Assert.notNull(object);
        Assert.notNull(property);
        try{
            Method method = getMethod(object.getClass(),getGetterMethodName(property));
            if(method==null){
                method = getMethod(object.getClass(),getIsMethodName(property));
            }
            if(method==null && property.startsWith("is")){
                property = property.substring(2);
                method = getMethod(object.getClass(),getIsMethodName(property));
            }
            if(method==null){
                throw new RuntimeException("没有找到 "+object.getClass().getName()+"."+getGetterMethodName(property)+"()方法 或"+getIsMethodName(property)+"()方法");
            }
            return (T) method.invoke(object);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 递归获取所有字段
     * @param clz
     * @param fieldList
     * @return
     */
    public static List<Field> getAllFields(Class<?> clz, List<Field> fieldList){
        if(clz==null || clz.getName().equals(Object.class.getName())){
            return fieldList;
        }
        if(fieldList==null){
            fieldList = new ArrayList<Field>();
        }
        Field[] fields = clz.getDeclaredFields();
        if(fields!=null && fields.length>0){
            for (Field field : fields) {
                //添加非静态的字段
                //todo 跳过子类复写的字段
                if(!Modifier.isStatic(field.getModifiers())){
                    fieldList.add(field);
                }
            }
        }
        return getAllFields(clz.getSuperclass(),fieldList);
    }

    /**
     * 根据字段名获取字段
     * @param clz
     * @param fieldName
     * @return
     */
    public static Field getField(Class clz, String fieldName){
        List<Field> fieldList = getAllFields(clz,null);
        for (Field field : fieldList) {
            if(field.getName().equals(fieldName)){
                return field;
            }
        }
        return null;
    }
    /**
     * 递归获取类的所有方法
     * @param clz
     * @param methodList
     * @return
     */
    public static List<Method> getAllMethods(Class clz, List<Method> methodList){
        if(clz==null || clz.getName().equals(Object.class.getName())){
            return methodList;
        }
        if(methodList==null){
            methodList = new ArrayList<Method>();
        }
        // 获得类的方法集合,todo 跳过子类复写的方法
        Method[] methods = clz.getDeclaredMethods();
        if(methods!=null && methods.length>0){
            methodList.addAll(Arrays.asList(methods));
        }
        return getAllMethods(clz.getSuperclass(),methodList);
    }

    /**
     * 根据类的方法名获取类的方法,包括父类的方法
     * @param clz
     * @param methodName
     * @return
     */
    public static Method getMethod(Class clz, String methodName){
        List<Method> methodList = getAllMethods(clz,null);
        for (Method method : methodList) {
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }

    /**
     * 根据不同类型转换成不同的类型的值
     * @param object
     * @param type
     * @return
     */
    public static Object getObjectValue(Object object, String type){

        if(StrKit.isEmpty(type) || StrKit.isEmpty(object)){
            return null;
        }
        String value;
        if(object instanceof String){
            value = object.toString();
        }else {
            value = JsonKit.toJSONString(object);
        }

        //去掉开头的class
        int index = type.toLowerCase().indexOf("class");
        if(index==0){
            type = type.substring(index+5).trim();
        }
        type = type.toLowerCase();
        //int
        if(type.equals("int")){
            return Integer.parseInt(value);
        }
        if(type.equals("java.lang.integer")){
            return Integer.valueOf(value);
        }
        //long
        if(type.equals("long")){
            return Long.parseLong(value);
        }
        if(type.equals("java.lang.long")){
            return Long.valueOf(value);
        }
        //double
        if(type.equals("double")){
            return Double.parseDouble(value);
        }
        if(type.equals("java.lang.double")){
            return Double.valueOf(value);
        }
        //short
        if(type.equals("short")){
          return Short.parseShort(value);
        }
        if(type.equals("java.lang.short")){
            return Short.valueOf(value);
        }
        //float
        if(type.equals("float")){
            return Float.parseFloat(value);
        }
        if(type.equals("java.lang.float")){
            return Float.valueOf(value);
        }
        //boolean
        if(type.equals("boolean") || type.equals("java.lang.boolean")){
            Integer integer = SafeKit.getInteger(value);
            if(integer==null){
                return Boolean.parseBoolean(value);
            }
            if(integer==0){
                return false;
            }
            if(integer>0){
                return true;
            }
        }
        //String
        if(type.equals("string") || type.equals("java.lang.string")){
            return value;
        }
        //BigDecimal
        if(type.equals("java.math.bigdecimal")){
            return new BigDecimal(value);
        }
        //byte
        if(type.equals("byte")){
            return Byte.parseByte(value);
        }
        if(type.equals("java.lang.byte")){
            return Byte.valueOf(value);
        }
        //byte数组
        if(type.equals("[b")){
            return value.getBytes();
        }
        if(type.equals("java.sql.date") || type.equals("java.util.date") || type.equals("java.sql.timestamp") || type.equals("java.sql.time")){
            Date date = null;
            if(value.indexOf(":")>0 && value.indexOf("-")>0){
                date = DateKit.getDate("yyyy-MM-dd HH:mm:ss", value);
            }else
            if(value.indexOf(":")==-1 && value.indexOf("-")>0){
                date = DateKit.getDate("yyyy-MM-dd", value);
            }else
            if(value.indexOf(":")==-1 && value.indexOf("/")>0){
                date = DateKit.getDate("yyyy/MM/dd", value);
            }else
            if(value.indexOf(":")>0 && value.indexOf("/")>0){
                date = DateKit.getDate("yyyy/MM/dd HH:mm:ss", value);
            }else
            if(value.indexOf("年")>0 && value.indexOf("秒")>0){
                date = DateKit.getDate("yyyy年MM月dd日  HH时mm分ss秒", value);
            }else
            if(value.indexOf("年")>0 && value.indexOf("秒")==-1){
                date = DateKit.getDate("yyyy年MM月dd日", value);
            }
            if(date == null){
                try{
                    date = new Date();
                    date.setTime(Long.valueOf(value));
                    return date;
                }catch (Exception e){}
            }
            return date;
        }
        return object;
    }

    /**
     * <p>Copy property values from the origin bean to the destination bean
     * for all cases where the property names are the same.</p>
     * @param sourceObj Origin bean whose properties are retrieved
     * @param targetObj Destination bean whose properties are modified
     */
    public static Object copyProperties(Object sourceObj, Object targetObj) {
        try {
            BeanUtils.copyProperties(sourceObj,targetObj);
            return targetObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断一个类是否为基本数据类型。
     * @param clazz 要判断的类。
     * @return true 表示为基本数据类型。
     */
    public static boolean isBaseDataType(Class<?> clazz){
        
        Set<Class<?>> primitiveClasses = new HashSet<Class<?>>();

        primitiveClasses.add(boolean.class);
        primitiveClasses.add(Boolean.class);

        primitiveClasses.add(char.class);
        primitiveClasses.add(Character.class);

        primitiveClasses.add(byte.class);
        primitiveClasses.add(Byte.class);

        primitiveClasses.add(short.class);
        primitiveClasses.add(Short.class);

        primitiveClasses.add(int.class);
        primitiveClasses.add(Integer.class);

        primitiveClasses.add(long.class);
        primitiveClasses.add(Long.class);

        primitiveClasses.add(float.class);
        primitiveClasses.add(Float.class);

        primitiveClasses.add(double.class);
        primitiveClasses.add(Double.class);

        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);

        primitiveClasses.add(String.class);
        primitiveClasses.add(Date.class);
        primitiveClasses.add(java.sql.Date.class);
        primitiveClasses.add(java.sql.Time.class);
        primitiveClasses.add(java.sql.Timestamp.class);

        return primitiveClasses.contains(clazz);
    }

    /**
     * 获取某个方法的所有参数名,接口的方法无法获取到
     * @param method 方法
     * @return 所有参数名
     */
    public static List<String> getMethodParamNames(Method method) {
        List<String> paramNameList = new ArrayList<String>();
        if(method==null){
            return null;
        }
        try {
            Class<?> clazz = method.getDeclaringClass();
            Class<?>[] paramsTypes = method.getParameterTypes();
            String[] classNames = new String[paramsTypes.length];
            for (int i = 0; i < paramsTypes.length; i++) {
                classNames[i] = paramsTypes[i].getName();
            }
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(clazz));
            CtClass cc = pool.get(clazz.getName());
            CtClass[] ccs = pool.get(classNames);

            CtMethod cm = cc.getDeclaredMethod(method.getName(), ccs);

            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            if (codeAttribute == null) {
                return null;
            }
            LocalVariableAttribute attr =
                    (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                return null;
            }
            String[] paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = attr.variableName(i + pos);
            }
            for (int i = 0; i < paramNames.length; i++) {
                paramNameList.add(paramNames[i]);
            }
        }catch (NotFoundException e){
            e.printStackTrace();
            return null;
        }
        return paramNameList;
    }

    /**
     * 获取基本类型的包装类型
     * @param cls
     * @return
     */
    public static Class primitiveToWrapper(Class cls) {
        Class convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = primitiveWrapperMap.get(cls);
        }
        return convertedClass;
    }

    private static Map<Class,Class> primitiveWrapperMap = new HashMap<>();
    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }
}
