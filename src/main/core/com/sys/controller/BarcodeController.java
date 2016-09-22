package com.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lenovo on 2014/12/13.
 */
@Controller
@RequestMapping("/barcode")
public class BarcodeController {
    @RequestMapping(params = "printBarcode")
    public String printBarcode(String[] msg,HttpServletRequest request){
        Assert.notEmpty(msg,"请求条形码为空！");
        request.setAttribute("msgList",msg);
        return "sys/barcode/barcode";
    }
}
