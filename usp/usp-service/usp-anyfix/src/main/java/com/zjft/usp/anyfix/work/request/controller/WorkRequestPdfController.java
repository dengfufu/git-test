package com.zjft.usp.anyfix.work.request.controller;

import com.zjft.usp.anyfix.utils.PdfUtil;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestPdfDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.service.WorkRequestPdfService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;


/**
 * 工单pdf服务请求
 * @author cxd
 * @since 2020-05-7
 */
@Api(tags = "工单pdf服务请求")
@RestController
@RequestMapping("/work-pdf")
public class WorkRequestPdfController {
    @Autowired
    private WorkRequestPdfService workRequestPdfService;

    /**
     * 工单导出pdf
     */
    @PostMapping(value = "/exportPDF")
    public void downLoadWorkPdf(HttpServletResponse response,@RequestBody WorkFilter workFilter,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam){
        WorkRequestPdfDto workRequestPdfDto = this.workRequestPdfService.getWorkRequestPdfDto(workFilter.getWorkId(),userInfo,reqParam);
        try {
            PdfUtil p = new PdfUtil();
            p.createPdf(response,workRequestPdfDto,userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 工单批量导出pdf
     */
    @PostMapping(value = "/list/exportPDF")
    public void downLoadWorkPdfList(HttpServletResponse response,@RequestBody WorkFilter workFilter,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam){
        workFilter.setWorkId(1258229191638405121L);
        WorkRequestPdfDto workRequestPdfDto = this.workRequestPdfService.getWorkRequestPdfDto(workFilter.getWorkId(),userInfo,reqParam);
        try {
            PdfUtil p = new PdfUtil();
            p.createPdf(response,workRequestPdfDto,userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
