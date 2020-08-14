package com.mepeng.cn.SevenPen.order.annotation;

import com.mepeng.cn.SevenPen.order.kryo.Dto.ParamsDto;
import com.mepeng.cn.SevenPen.order.kryo.Dto.PenHimlyEntity;
import com.mepeng.cn.SevenPen.order.mapper.DemoMapper;
import com.mepeng.cn.SevenPen.order.util.ObjectByteUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

/**
 * PenHmily 切面
 */
@Component
@Aspect
public class PenHmilyAspect {
    @Autowired
    DemoMapper demoMapper;
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PenHmilyAspect.class);
    public PenHmilyAspect(){
        System.out.println("PenHmilyAspect is init doing");
    }
    /**
     * 切入点
     */
    @Pointcut("@annotation(com.mepeng.cn.SevenPen.order.annotation.PenHmily) ")
    public void entryPoint() {
        // 无需内容
    }

    @Before("entryPoint()")
    public void before(JoinPoint point) {
        LOGGER.info("=================PenHmilyAspect before ==========================");
        System.out.println("=================PenHmilyAspect before ==========================");
    }

    @After("entryPoint()")
    public void after(JoinPoint point) {
        LOGGER.info("=================PenHmilyAspect after ==========================");
        System.out.println("=================PenHmilyAspect after ==========================");
    }

    @AfterThrowing(pointcut = "entryPoint()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Throwable e) {
        LOGGER.info("=================PenHmilyAspect doAfterThrowing ==========================",e);
        System.out.println("=================PenHmilyAspect doAfterThrowing =========================="+e.getMessage());
        String targetName = point.getTarget().getClass().getName();//执行的类
        String methodName = point.getSignature().getName();//执行的方法名

        Object[] arguments = point.getArgs();
        Class<?> cls = point.getTarget().getClass();
        try {
            Method[] methods = cls.getMethods();
            for(Method method:methods){
                if(method.getName().equals(methodName)){
                    if(method.isAnnotationPresent(PenHmily.class)){
                       //拥有此注解的忽略
                        PenHmily penHmily = method.getAnnotation(PenHmily.class);
                        String confirm = penHmily.confirmMethod();
                        String cancel = penHmily.cancelMethod();
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if(parameterTypes.length==arguments.length){
                            boolean flag = false;
                            for(int i=0;i<parameterTypes.length;i++){
                                Class<?> parameterType = parameterTypes[i];
                                Object arg = arguments[i];
                                Class<?> argCls = arg.getClass();
                                System.out.println("argCls.getName():"+argCls.getName()+",parameterType.getName():"+parameterType.getName());
                                System.out.println("形参是否是实际参数父类或者接口："+parameterType.isAssignableFrom(argCls));
                                if(!parameterType.isAssignableFrom(argCls)){
                                    //实际参数不是形参的父类或者接口 不是该方法
                                    flag = true;
                                }

                            }
                            if(!flag){
                                System.out.println("就是运行的方法");
                                PenHimlyEntity entity = new PenHimlyEntity();
                                entity.setTargetClass(targetName);
                                entity.setTargetMethod(methodName);
                                entity.setConfirmMethod(confirm);
                                entity.setCancelMethod(cancel);
                                entity.setCreateTime(new Date());

                                ParamsDto dto = new ParamsDto();
                                dto.setArgs(arguments);
                                dto.setParameterTypes(parameterTypes);
                                dto.setCls(cls);
                                dto.setMethodName(methodName);
                                byte[] bytes = ObjectByteUtil.objToBytes(dto);
                                System.out.println("就是运行的方法 bytes:"+bytes.length);
                                entity.setData(bytes);
                                System.out.println("就是运行的方法 entity:"+entity);
                                int affect = demoMapper.insertPenHimlyEntity(entity);
                                System.out.println("就是运行的方法 insert affect:"+affect+",id:"+entity.getId());
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //cls.get
        System.out.println("=================PenHmilyAspect doAfterThrowing ==========================targetName:"+targetName+",methodName"+methodName);
    }

    public static void main(String[] args) {
        System.out.println("234");
        Class<?> cls = PenHmilyAspect.class;
        try {
            String methodName = "doAfterThrowing";
            Method[] methods = cls.getMethods();
            for(Method method:methods){
                if(method.getName().equals(methodName)){
                    System.out.println("method:"+methods);
                    if(!method.isAnnotationPresent(AfterThrowing.class)) return; //3.判断是否有注解:如果没有就直接返回

                    AfterThrowing annotation = method.getAnnotation(AfterThrowing.class); //4.获取在该方法上的注解

                    System.out.println("pointcut="+annotation.pointcut());
                    System.out.println("nodefault="+annotation.throwing());
                    System.out.println("novalue="+annotation.argNames());
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Parameter[] pas = method.getParameters();
                    for(int i=0;i<parameterTypes.length;i++){
                        Class<?> parameterType = parameterTypes[i];
                        System.out.println("parameterType.name:"+parameterType.getName()+",type:"+parameterType.getClass());
                    }
                    for(Parameter pa:pas){
                        System.out.println("pa.name:"+pa.getName()+",type:"+pa.getType()+",t1:"+pa.getType().getName());
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Around("entryPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        LOGGER.info("=================PenHmilyAspect around ==========================");
        System.out.println("=================PenHmilyAspect around ==========================");
        Object result = point.proceed();
        System.out.println("=================PenHmilyAspect around ==========================point.proceed()："+result);
        return result;
    }
}

