package com.zjft.usp.device.device.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.zjft.usp.device.device.service.DeviceExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 设备导入sheet写入
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-28 15:01
 **/
@Service
public class DeviceSheetWriteHandler implements SheetWriteHandler {

    @Autowired
    private DeviceExcelService deviceExcelService;

    // 委托商
    private Long demanderCorp;

    public Long getDemanderCorp() {
        return this.demanderCorp;
    }

    public void setDemanderCorp(Long demanderCorp) {
        this.demanderCorp = demanderCorp;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Map<Integer, String[]> mapDropDown = this.deviceExcelService.getMapDropDown(this.demanderCorp);
        Sheet sheet = writeSheetHolder.getSheet();
        // 开始设置下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
            if(entry.getValue().length < 1) {
                continue;
            }
            /***起始行、终止行、起始列、终止列**/
            CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, entry.getKey(), entry.getKey());
            /***设置下拉框数据**/
            DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            /***处理Excel兼容性问题**/
            if (dataValidation instanceof XSSFDataValidation) {
                if(entry.getKey() == 14) {
                    dataValidation.setShowErrorBox(false);
                    dataValidation.setSuppressDropDownArrow(true);
                } else {
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.setShowErrorBox(true);
                }
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(dataValidation);
        }
    }

    /**
     * 名称管理器
     *
     * @param wb
     * @param name
     * @param expression
     * @return
     */
    private Name createName(Workbook wb, String name, String expression){
        Name refer = wb.createName();
        refer.setRefersToFormula(expression);
        refer.setNameName(name);
        return refer;
    }

    /**
     * 设置数据有效性
     * @param name
     * @param firstRow
     * @param endRow
     * @param firstCol
     * @param endCol
     * @return
     */
//    private DataValidation setDataValidation(DataValidationHelper helper, String name, int firstRow, int endRow, int firstCol, int endCol){
//        DataValidationConstraint dvConstraint = helper.createFormulaListConstraint(name);
//        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);
//        DataValidation validation = helper.createValidation(dvConstraint, regions);
//        return validation;
//    }
//
//    public String getRange(int offset, int rowId, int colCount) {
//        char start = (char)('A' + offset);
//        if (colCount <= 25) {
//            char end = (char)(start + colCount - 1);
//            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
//        } else {
//            char endPrefix = 'A';
//            char endSuffix = 'A';
//            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
//                if ((colCount - 25) % 26 == 0) {// 边界值
//                    endSuffix = (char)('A' + 25);
//                } else {
//                    endSuffix = (char)('A' + (colCount - 25) % 26 - 1);
//                }
//            } else {// 51以上
//                if ((colCount - 25) % 26 == 0) {
//                    endSuffix = (char)('A' + 25);
//                    endPrefix = (char)(endPrefix + (colCount - 25) / 26 - 1);
//                } else {
//                    endSuffix = (char)('A' + (colCount - 25) % 26 - 1);
//                    endPrefix = (char)(endPrefix + (colCount - 25) / 26);
//                }
//            }
//            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
//        }
//    }

}
