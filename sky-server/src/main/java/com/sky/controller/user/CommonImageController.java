package com.sky.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片访问相关接口（小程序通过该接口渲染菜品/套餐图片）
 */
@RestController("commonImageController")
@RequestMapping("/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonImageController {

    /**
     * 图片下载：菜品图片本身是 OSS 地址，直接重定向到图片地址
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    @ApiOperation("图片下载")
    public void download(String name, HttpServletResponse response) throws IOException {
        if (name != null && (name.startsWith("http://") || name.startsWith("https://"))) {
            response.sendRedirect(name);
        }
    }
}
