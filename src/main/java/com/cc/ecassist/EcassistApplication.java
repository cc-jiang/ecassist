package com.cc.ecassist;

import com.cc.ecassist.constant.Constant;
import com.cc.ecassist.utils.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author congcong.jiang
 * @description springboot启动程序
 * @date 2023/9/15 0:08
 */
@SpringBootApplication
public class EcassistApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcassistApplication.class, args);

        FileUtils.createDir(Constant.PATH);
    }
}
