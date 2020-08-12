package com.zjft.usp.device.device.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.constant.ExcelCodeMsg;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.ExcelUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.device.dto.DeviceExcelDto;
import com.zjft.usp.device.device.dto.DeviceExportDto;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;
import com.zjft.usp.device.device.handler.DeviceSheetWriteHandler;
import com.zjft.usp.device.device.service.DeviceExcelService;
import com.zjft.usp.uas.service.UasFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备excel控制器
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-27 13:58
 **/
@Api(tags = "设备excel控制器")
@RestController
@RequestMapping("/device-excel")
public class DeviceExcelController {

    @Autowired
    private DeviceExcelService deviceExcelService;
    @Autowired
    private DeviceSheetWriteHandler deviceSheetWriteHandler;
    @Resource
    private UasFeignService uasFeignService;

    public static final int MAX_IMPORT_NUMBER = 10000;
    public static final String TEMPLATE_NAME = "设备导入模板";

    @ApiOperation("下载导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response, @CommonReqParam ReqParam reqParam,
                                 @RequestParam("demanderCorp") Long demanderCorp) throws IOException {
        if (LongUtil.isZero(demanderCorp)) {
            demanderCorp = reqParam.getCorpId();
        }
        Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(
                JsonUtil.toJson(Collections.singleton(demanderCorp)));
        Map<Long, String> corpMap = corpMapResult == null ? new HashMap<>() : corpMapResult.getData();

        String fileName = URLEncoder.encode(corpMap.get(demanderCorp) + TEMPLATE_NAME, "UTF-8");

        List<DeviceExcelDto> deviceExcelDtoList = this.deviceExcelService.getDeviceTemplate(demanderCorp);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 设置下拉框数据
        deviceSheetWriteHandler.setDemanderCorp(demanderCorp);
        try {
            EasyExcel.write(response.getOutputStream(), DeviceExcelDto.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(TEMPLATE_NAME)
                    .registerWriteHandler(deviceSheetWriteHandler)
                    .doWrite(deviceExcelDtoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("导入设备档案")
    @PostMapping("/import")
    public Result importDevice(@RequestBody MultipartFile file, @CommonReqParam ReqParam reqParam,
                               @LoginUser UserInfo userInfo, @RequestParam("demanderCorp") Long demanderCorp) {
        List<DeviceExcelDto> deviceExcelDtoList = null;
        try {
            deviceExcelDtoList = EasyExcel.read(new BufferedInputStream(file.getInputStream())).head(
                    DeviceExcelDto.class).sheet().doReadSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 最大行数校验
        if (deviceExcelDtoList.size() > MAX_IMPORT_NUMBER) {
            return Result.failed(ExcelCodeMsg.OVER_MAX_USER_IMPORT_LIMIT.fillArgs(MAX_IMPORT_NUMBER).getMsg());
        }
        if (LongUtil.isZero(demanderCorp)) {
            demanderCorp = reqParam.getCorpId();
        }
        // 校验数据
        String msgStr = this.deviceExcelService.checkImportData(deviceExcelDtoList, userInfo, demanderCorp);
        if (!StringUtils.isEmpty(msgStr)) {
            return Result.succeed("导入失败！错误信息：<br>" + msgStr);
        } else {
            return Result.succeed("导入成功！共导入" + deviceExcelDtoList.size() + "条记录");
        }
    }

    @PostMapping(value = "/export")
    public void downLoadDeviceExcel(HttpServletResponse response,@RequestBody DeviceInfoFilter deviceInfoFilter,
                                  @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam){
        String fileName = "设备档案";
        String sheetName = "设备记录";
        List<DeviceExportDto> deviceExcelList = this.deviceExcelService.getDeviceExcelList(deviceInfoFilter,userInfo,reqParam);
        try {
            ExcelUtil.writeExcel(response, deviceExcelList, fileName, sheetName, DeviceExportDto.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
