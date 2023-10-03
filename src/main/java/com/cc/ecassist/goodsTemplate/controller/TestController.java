package com.cc.ecassist.goodsTemplate.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.cc.ecassist.goodsTemplate.domain.ExportVO;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author congcong.jiang
 * @description test
 * @date 2023/9/15 0:17
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("importExcel")
    public void importExcel(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        String fileName = "C://ecassist//test.xlsx";
        EasyExcel.read(fileName, ExportVO.class, new ReadListener() {
            @Override
            public void invoke(Object data, AnalysisContext context) {

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet().doRead();
    }

    @GetMapping("exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "test" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, ExportVO.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });

    }

    private List<ExportVO> data() {
        List<ExportVO> list = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            ExportVO exportVO = new ExportVO();
            exportVO.setName("name" + i);
            exportVO.setValue(new BigDecimal(123.11 + i * 0.09).toString());
            exportVO.setAmount(new BigDecimal(6666.666 + i * 10));
            exportVO.setDate(new Date(132324343 + i * 100));
            exportVO.setDateTime(new Date());
            list.add(exportVO);
        }
        return list;
    }

    @GetMapping("exportExcelLocal")
    public void exportExcelLocal(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "C://ecassist//test" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, ExportVO.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });
    }

    @GetMapping("writeFile")
    public String writeFile(String content) throws IOException {
        File file = ResourceUtils.getFile("classpath:file/test.txt");
        FileUtils.write(file, content, Charset.defaultCharset());
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    @GetMapping("readFile")
    public String readFile() throws IOException {
        Resource resource = new ClassPathResource("file/test.txt");
//        File file = ResourceUtils.getFile("classpath:file/test.txt");
//        return FileUtils.readFileToString(file, Charset.defaultCharset());
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println("error");
        }
        return sb.toString();
    }
}
