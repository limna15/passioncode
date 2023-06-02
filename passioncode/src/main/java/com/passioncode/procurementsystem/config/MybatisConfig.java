package com.passioncode.procurementsystem.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration //설정파일임을 알려줌
@MapperScan(basePackages = {"com.passioncode.procurementsystem.mapper"}) //해당 패키지를 Mapper로 인식해라
public class MybatisConfig {

}
