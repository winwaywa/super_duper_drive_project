package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadExceptionAdvice.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, @NotNull RedirectAttributes redirectAttributes) {
        logger.error(exc.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Kích thước file tải lên vượt quá giới hạn cho phép!");
        return "redirect:/home";
    }
}
