package com.cc.ecassist;

import com.cc.ecassist.goodsTemplate.constant.PathConstant;
import com.cc.ecassist.utils.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot启动程序
 *
 * @author congcong.jiang
 * @date 2023-9-15
 */
@SpringBootApplication
public class EcassistApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcassistApplication.class, args);

        FileUtils.createDir(PathConstant.PATH);
    }
}
