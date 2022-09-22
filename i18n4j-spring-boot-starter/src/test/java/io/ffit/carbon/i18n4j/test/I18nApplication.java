package io.ffit.carbon.i18n4j.test;

import io.ffit.carbon.i18n4j.core.I18nSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SpringBoot Application
 *
 * @author Lay
 */
@SpringBootApplication
@RestController
@RequestMapping("/")
public class I18nApplication {

    public static void main(String[] args) {
        SpringApplication.run(I18nApplication.class);
    }

    @Resource
    I18nSource i18nSource;

    @GetMapping("getMessage")
    public String getMessage(@RequestParam("key") String key) {
        return i18nSource.getMessage(key, LocaleContextHolder.getLocale());
    }
}
