package com.mepeng.cn.SevenPen.order.excel;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

@Service
public class ValidateServiceImpl implements ValidateService {
    public ValidateServiceImpl() {
    }

    public <T> String validateObject(T tinstance) {
        return this.validateObject(tinstance, new ValidateMessageCallBack() {
            public String messageCallBack(String constinatField, String message, String defaultMessage) {
                return message != null ? message : constinatField + ":" + defaultMessage;
            }
        });
    }

    public <T> String validateObject(T tinstance, ValidateMessageCallBack messageCallBack) {
        ValidatorFactory validatorFactory = null;//(ValidatorFactory)ApplicationContextHelper.getBeanByType(ValidatorFactory.class);
        Validator validator = validatorFactory.getValidator();
        ResourceBundleMessageSource messageSource = null;//(ResourceBundleMessageSource)ApplicationContextHelper.getBeanByType(ResourceBundleMessageSource.class);
        Locale locale = null;

//        try {
//            LoginInfoDto loginInfo = (LoginInfoDto)ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
//            locale = loginInfo.getLocale();
//        } catch (Exception var19) {
//            locale = new Locale("zh_CN");
//        }

        String className = tinstance.getClass().getSimpleName();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(tinstance, new Class[0]);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            StringBuilder errorMsg = new StringBuilder();
            Iterator var10 = constraintViolations.iterator();

            while(var10.hasNext()) {
                ConstraintViolation<T> constraintViolation = (ConstraintViolation)var10.next();
                String consstraintName = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
                String constinatField = constraintViolation.getPropertyPath().toString();
                String newCode = consstraintName + "." + className + "." + constinatField;
                String message = null;
                String defaultMessage = null;

                try {
                    message = messageSource.getMessage(newCode, (Object[])null, locale);
                } catch (NoSuchMessageException var18) {
                    //logger.error(var18.getMessage());
                    var18.printStackTrace();
                    defaultMessage = constraintViolation.getMessage();
                }

                String returnMessage = messageCallBack.messageCallBack(constinatField, message, defaultMessage);
               // if (!StringUtils.isNullOrEmpty(returnMessage)) {
                if(!(returnMessage==null||"".equals(returnMessage))){
                    errorMsg.append(returnMessage).append(" ; ");
                }
            }

            return errorMsg.toString();
        } else {
            return null;
        }
    }

    public <T> boolean isValidObject(T instance) {
        String errorMessage = this.validateObject(instance);
        //if (!StringUtils.isNullOrEmpty(errorMessage)) {
        if(!(errorMessage==null||"".equals(errorMessage))){
            throw new RuntimeException("数据校验失败:" + errorMessage);
        } else {
            return true;
        }
    }
}
