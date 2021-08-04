package com.myself.server.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 全局日期格式转换类
 *
 * @author Wei
 * @since 2021/6/3
 */
@Component
public class DateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String s) {
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(s, df);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
