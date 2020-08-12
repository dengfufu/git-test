package com.zjft.usp.anyfix.work.request.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.zjft.usp.anyfix.work.request.service.WorkRequestExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ljzhu
 * @Package com.zjft.usp.anyfix.work.request.handler
 * @date 2020-02-25 20:57
 * @note
 */
@Service
public class SpinnerWriteHandler implements SheetWriteHandler {

    private  Long demanderCorp;

    @Autowired
    private WorkRequestExcelService workRequestExcelService;

    public Long getDemanderCorp() {
        return demanderCorp;
    }

    public void setDemanderCorp(Long demanderCorp) {
        this.demanderCorp = demanderCorp;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Map<Integer,String []> mapDropDown = this.workRequestExcelService.getMapDropDown(this.demanderCorp);
        Sheet sheet = writeSheetHolder.getSheet();
        ///开始设置下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
            /***起始行、终止行、起始列、终止列**/
            CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, entry.getKey(), entry.getKey());
            /***设置下拉框数据**/
            DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            /***处理Excel兼容性问题**/
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(dataValidation);
        }

    }

}
