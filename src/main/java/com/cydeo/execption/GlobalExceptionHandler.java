package com.cydeo.execption;

import com.cydeo.annotation.AccountingExceptionMessage;
import com.cydeo.dto.DefaultExceptionMessageDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Throwable.class})
    public String genericException(Throwable exception, HandlerMethod handlerMethod, Model model) {
        String errorMessage = getMessageFromAnnotation(handlerMethod.getMethod())
                .map(DefaultExceptionMessageDto::getMessage)
                .orElse(Optional.ofNullable(exception.getMessage()).orElse("Something went wrong!"));

        model.addAttribute("message", errorMessage);
        return "error";
    }

    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(Method method) {
        if (method.isAnnotationPresent(AccountingExceptionMessage.class)) {
            AccountingExceptionMessage annotation = method.getAnnotation(AccountingExceptionMessage.class);
            DefaultExceptionMessageDto messageDto = new DefaultExceptionMessageDto(annotation.message());
            return Optional.of(messageDto);
        }
        return Optional.empty();
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public String handleCategoryNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }
    @ExceptionHandler(ClientVendorNotFoundException.class)
    public String handleClientVendorNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public String handleCompanyNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public String handleInvoiceNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

    @ExceptionHandler(InvoiceProductNotFoundException.class)
    public String handleInvoiceProductNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }
    @ExceptionHandler(ProductLowLimitAlertException.class)
    public String handleProductLowLimitAlertException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public String handleRoleNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "message";
    }

}
