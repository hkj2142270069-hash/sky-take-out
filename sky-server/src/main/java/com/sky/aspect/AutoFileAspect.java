package com.sky.aspect;

import com.sky.annotation.AutoFile;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFileAspect {
 /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFile)")
    public void autoFilePointcut(){}

    /**
     * 前置通知，在方法执行前执行
     */
    @Before("autoFilePointcut()")
    public void autoFile(JoinPoint joinPoint){
        log.info("开始进行公共字段的自动填充");
        //获取当前被拦截的方法的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFile autoFile = signature.getMethod().getAnnotation(AutoFile.class);
        OperationType value = autoFile.value();
       //  1. **获取方法签名**：通过 `joinPoint.getSignature()` 获取当前执行方法的签名，并强制转换为 `MethodSignature` 类型。
       // 2. **获取注解信息**：从方法签名中获取方法对象，并检查该方法是否被 [@AutoFile](file://G:\cqwm\code\sky-take-out\sky-server\src\main\java\com\sky\annotation\AutoFile.java#L12-L20) 注解标记。
       // 3. **提取操作类型**：如果方法有 [@AutoFile](file://G:\cqwm\code\sky-take-out\sky-server\src\main\java\com\sky\annotation\AutoFile.java#L12-L20) 注解，则获取注解中定义的 [OperationType](file://G:\cqwm\code\sky-take-out\sky-common\src\main\java\com\sky\enumeration\OperationType.java#L5-L17) 值（如 [INSERT](file://G:\cqwm\code\sky-take-out\sky-common\src\main\java\com\sky\enumeration\OperationType.java#L15-L15) 或 [UPDATE](file://G:\cqwm\code\sky-take-out\sky-common\src\main\java\com\sky\enumeration\OperationType.java#L10-L10)）。

        //获取当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        Object entity = args[0];


        //准备赋值
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        if (value == OperationType.INSERT) {
            //为4个公共字段赋值
         try {
             Method serCreateTime= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
             Method setUpdateTime= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
             Method setCreateUser= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
             Method setUpdateUser= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
             serCreateTime.invoke(entity, now);
             setUpdateTime.invoke(entity, now);

             setCreateUser.invoke(entity, currentId);
             setUpdateUser.invoke(entity, currentId);

         }catch (Exception e){
             e.printStackTrace();
         }

        }
        else if (value == OperationType.UPDATE) {
            try {
                Method setUpdateTime= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        //根据当前不同的操作类型为对应属性通过反射来赋值

    }
}
