package com.flipped.potato.framework.log.core.aspect;

import com.flipped.potato.framework.common.entity.api.PotatoResult;
import com.flipped.potato.framework.common.util.http.HttpContextUtil;
import com.flipped.potato.framework.common.util.http.IpUtil;
import com.flipped.potato.framework.common.util.json.JsonUtils;
import com.flipped.potato.framework.log.core.annotation.OperationLog;
import com.flipped.potato.framework.log.core.entity.OperationLogEntity;
import com.flipped.potato.framework.log.core.service.OperationLogService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * 日志切面
 * 在执行目标方法之前执行该切面，记录请求日志
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2022-04-11 19:35:16
 */
@Slf4j
@Aspect
public class OperationLogAspect {

    private static final String USER_ID = "userid";
    private static final String USER_NAME = "username";

    private final StopWatch stopWatch = new StopWatch("operationLog");

    @Resource
    private OperationLogService operationLogService;

    /**
     * 设置自定义注解相关的日志信息
     *
     * @param point              切面信息
     * @param operationLogEntity 日志信息
     */
    private static void setLogWithAnnotation(ProceedingJoinPoint point, OperationLogEntity operationLogEntity) {
        // 获取当前方法的信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        OperationLog operationLogAnnotation = method.getAnnotation(OperationLog.class);
        if (operationLogAnnotation != null) {
            operationLogEntity.setMethodType(operationLogAnnotation.type().getType());
            operationLogEntity.setMethodDescription(operationLogAnnotation.value());
            operationLogEntity.setModule(operationLogAnnotation.module());
        }
    }

    /**
     * 设置和操作相关的日志信息
     *
     * @param request            请求信息
     * @param result             请求返回结果
     * @param operationLogEntity 日志信息
     * @param time               处理时长
     */
    private static void setLogWithOperator(HttpServletRequest request, Object result, OperationLogEntity operationLogEntity, long time) {
        if (StringUtils.isNotBlank(request.getAttribute(USER_ID).toString())) {
            operationLogEntity.setOperationUserid(Long.parseLong(request.getAttribute(USER_ID).toString()));
        }
        if (StringUtils.isNotBlank(request.getAttribute(USER_NAME).toString())) {
            operationLogEntity.setOperationUsername(request.getAttribute(USER_NAME).toString());
        }
        operationLogEntity.setSpendTime(time);
        operationLogEntity.setOperationTime(LocalDateTime.now());

        // 请求是否成功
        if (result instanceof PotatoResult<?> potatoResult) {
            if (potatoResult.getSuccess()) {
                operationLogEntity.setSuccess(1);
            } else {
                operationLogEntity.setSuccess(0);
            }
        }
    }

    @Pointcut("@annotation(com.flipped.potato.framework.log.core.annotation.OperationLog)")
    public void operationLog() {
    }

    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        // 方法执行前执行的操作，记录开始时间
        stopWatch.start();

        // 执行目标方法
        Object result = point.proceed();

        // 方法执行后执行的操作，记录结束时间，并记录日志
        stopWatch.stop();
        long time = stopWatch.getTotalTimeMillis();

        // 保存日志
        try {
            saveLog(point, result, time);
        } catch (Exception e) {
            // 记录日志错误不要影响业务
            log.error("log record parse exception", e);
        }

        return result;
    }

    /**
     * 保存日志
     *
     * @param point  方法信息
     * @param result 执行目标方法返回对象
     * @param time   执行时间
     */
    private void saveLog(ProceedingJoinPoint point, Object result, long time) {
        OperationLogEntity operationLogEntity = new OperationLogEntity();

        // 获取request对象
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();

        // 设置和自定义注解相关的日志信息
        setLogWithAnnotation(point, operationLogEntity);

        // 设置和请求相关的日志信息
        setLogWithRequest(point, request, operationLogEntity);

        // 操作信息
        setLogWithOperator(request, result, operationLogEntity, time);

        // 把日志放进消息队列
        // kafkaProducerService.sendMessage("user-logs", JsonUtil.bean2Json(platformLog));

        // 把日志放进es
        // esSaveService.savePlatformLog2Es(platformLog);

        // 把日志放进数据库
        operationLogService.save(operationLogEntity);

        log.info("日志输出: {}", JsonUtils.bean2Json(operationLogEntity));
    }

    /**
     * 设置请求相关的日志信息
     *
     * @param point              切面信息
     * @param request            请求信息
     * @param operationLogEntity 日志信息
     */
    private void setLogWithRequest(ProceedingJoinPoint point, HttpServletRequest request, OperationLogEntity operationLogEntity) {
        // 获取当前方法的信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 请求路由信息、入参信息
        operationLogEntity.setRequestUri(request.getRequestURI());
        operationLogEntity.setRequestUrl(String.valueOf(request.getRequestURL()));
        operationLogEntity.setRequestMethod(request.getMethod());
        operationLogEntity.setRequestParams(JsonUtils.bean2Json(getParams(method, point.getArgs())));

        // 类信息、方法信息
        operationLogEntity.setClassName(point.getTarget().getClass().getName());
        operationLogEntity.setMethodName(method.getName() + "()");

        // ip信息
        operationLogEntity.setServerIp(IpUtil.getServiceIpByNetCard());
        operationLogEntity.setClientIp(IpUtil.getClientIp(request));
    }

    /**
     * 根据方法和传入的参数获取请求参数
     *
     * @param method       方法信息
     * @param paramsValues 请求参数值列表
     */
    private Object getParams(Method method, Object[] paramsValues) {
        Map<String, Object> params = new HashMap<>(8);
        // 获取请求参数键集合
        Parameter[] paramsKeys = method.getParameters();

        for (int i = 0; i < paramsKeys.length; i++) {
            // 将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = paramsKeys[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                return paramsValues[i];
            }

            // 将@RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = paramsKeys[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                assembleParams(paramsKeys, paramsValues, i, requestParam.value(), params);
            }

            // 将@PathVariable注解修饰的参数作为请求参数
            PathVariable pathVariable = paramsKeys[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                assembleParams(paramsKeys, paramsValues, i, pathVariable.value(), params);
            }

            // 将@ModelAttribute注解修饰的参数作为请求参数
            ModelAttribute modelAttribute = paramsKeys[i].getAnnotation(ModelAttribute.class);
            if (modelAttribute != null) {
                assembleParams(paramsKeys, paramsValues, i, modelAttribute.value(), params);
            }

        }

        return params;
    }


    /**
     * TODO：此处需要优化，需要应对不同的参数类型
     * 组装请求参数
     *
     * @param paramsKeys   参数键列表
     * @param paramsValues 参数值列表
     * @param params       最终返回参数列表
     * @param i            遍历计数器
     * @param value        注解标注值，例如@RequestParam(value = "xxx")
     */
    private void assembleParams(Parameter[] paramsKeys, Object[] paramsValues, int i, String value, Map<String, Object> params) {
        String key = paramsKeys[i].getName();
        // 如果注解中设置了键值，则覆盖默认的属性名
        if (StringUtils.isNotBlank(value)) {
            key = value;
        }
        params.put(key, paramsValues[i]);
    }
}
