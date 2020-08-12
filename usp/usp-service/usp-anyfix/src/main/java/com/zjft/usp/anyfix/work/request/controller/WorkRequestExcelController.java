package com.zjft.usp.anyfix.work.request.controller;

import com.alibaba.excel.EasyExcel;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestExcelFileDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestExportDto;
import com.zjft.usp.anyfix.work.request.enums.ExcelCodeMsg;
import com.zjft.usp.anyfix.utils.ExcelUtil;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestExcelDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.handler.SpinnerWriteHandler;
import com.zjft.usp.anyfix.work.request.service.WorkRequestExcelService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.util.*;


/**
 * 工单excel服务请求
 * @author ljzhu
 * @since 2020-02-25
 */
@Api(tags = "工单excel服务请求")
@RestController
@RequestMapping("/work-excel")
public class WorkRequestExcelController {


    @Autowired
    private WorkRequestExcelService workRequestExcelService;

    @Autowired
    private SpinnerWriteHandler spinnerWriteHandler;


    public static final int MAX_WORK_IMPORT = 1000;

    /**
     * 下载Excel模板
     */
    @GetMapping(value = "/template")
    public void downloadTemplate(HttpServletResponse response,@RequestParam("demanderCorp") Long demanderCorp ,
                                   @RequestParam("demanderCorpName") String demanderCorpName) {
        String fileName = "导入工单模板";
        String sheetName = "导入工单模板";
        List<WorkRequestExcelDto> workRequestExcelDtoList = this.workRequestExcelService.getTemplateWorkRequestExcelDtoList(demanderCorp,demanderCorpName);
        try {
            spinnerWriteHandler.setDemanderCorp(demanderCorp);
            ExcelUtil.writeExcel(response, workRequestExcelDtoList, fileName, sheetName, WorkRequestExcelDto.class, spinnerWriteHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/export")
    public void downLoadWorkExcel(HttpServletResponse response,@RequestBody WorkFilter workFilter,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam){
        String fileName = "工单";
        String sheetName = "工单记录";
        List<WorkRequestExportDto> workRequestExcelDtoList = this.workRequestExcelService.getWorkRequestExportDtoList(workFilter,userInfo,reqParam);
        try {
            ExcelUtil.writeExcel(response, workRequestExcelDtoList, fileName, sheetName, WorkRequestExportDto.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导入工单
     * @param workRequestExcelFileDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    @PostMapping(value = "/import")
    public Result importData(@ModelAttribute WorkRequestExcelFileDto workRequestExcelFileDto, @LoginUser UserInfo userInfo,
                           @CommonReqParam ReqParam reqParam){

        Map<String,Object> returnInfo = new HashMap<>();
        returnInfo.put("num",0);

        if(workRequestExcelFileDto == null || workRequestExcelFileDto.getFile() == null){
            returnInfo.put("errorInfo",ExcelCodeMsg.FILE_NULL.fillArgs().getMsg());
            return Result.succeed(returnInfo);

        }

        List<WorkRequestExcelDto> workRequestExcelDtoList = null;
        // 1.excel同步读取数据
        try {
            MultipartFile file = workRequestExcelFileDto.getFile();
            workRequestExcelDtoList = EasyExcel.read(new BufferedInputStream(file.getInputStream())).
                    head(WorkRequestExcelDto.class).sheet().doReadSync();
        } catch (Exception e) {
            returnInfo.put("errorInfo",ExcelCodeMsg.FILE_FORMAT.fillArgs().getMsg());
            return Result.succeed(returnInfo);
        }
        if (workRequestExcelDtoList.size() == 0) {
            returnInfo.put("errorInfo",ExcelCodeMsg.FILE_IS_EAMPTY.fillArgs().getMsg());
            return Result.succeed(returnInfo);
        }
        // 2.检查是否大于1000条
        if (workRequestExcelDtoList.size() > MAX_WORK_IMPORT) {
            returnInfo.put("errorInfo",ExcelCodeMsg.OVER_MAX_USER_IMPORT_LIMIT.fillArgs(MAX_WORK_IMPORT).getMsg());
            return Result.succeed(returnInfo);
        }
        // 3.导入校验所有行列格式
        try{
            List<Long> workIdList = workRequestExcelService.checkImportData(workRequestExcelDtoList, userInfo, reqParam);
            workRequestExcelService.sendImportWorkMessage(workIdList, workRequestExcelFileDto.getAutoHandel());
            returnInfo.put("num",workIdList.size());
            returnInfo.put("errorInfo","");
            return Result.succeed(returnInfo);
        }catch (Exception e){
            returnInfo.put("errorInfo",e.getMessage());
            return Result.succeed(returnInfo);
        }
    }

}
