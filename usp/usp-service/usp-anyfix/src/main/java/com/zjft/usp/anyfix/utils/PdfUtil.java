package com.zjft.usp.anyfix.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDto;
import com.zjft.usp.anyfix.settle.enums.SettleWayEnum;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestPdfDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.file.service.FileFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * @author cxd
 * @Package com.zjft.usp.anyfix.utils
 * @date 2020-05-7 10:24
 * @note
 */
@Component
public class PdfUtil {
    private static PdfUtil pdfUtil;
    @Autowired
    private FileFeignService fileFeignService;

    private static final int Text_Font_Size = 10;
    private static final int Head_Font_Size = 16;

    @PostConstruct
    private void init(){
        pdfUtil = this;
        pdfUtil.fileFeignService = this.fileFeignService;
    }

    /**
     * 工单导出pdf
     * @date 2020/5/8
     * @param response
     * @return void
     */
    public  void createPdf(HttpServletResponse response, WorkRequestPdfDto workRequestPdfDto, UserInfo userInfo) throws Exception {
        try{
            // 设置response参数，请求返回类型，可以打开下载页面
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(
                        workRequestPdfDto.getWorkTypeName() + "服务记录单" + workRequestPdfDto.getWorkCode() + ".pdf", "UTF-8"));
            //1、创建流对象
            PdfWriter pdfWriter=new PdfWriter(response.getOutputStream());

            //2、创建文档对象
            PdfDocument pdfDocument=new PdfDocument(pdfWriter);

            //3、创建内容文档对象
            Document document=new Document(pdfDocument, PageSize.A4);

            //4.初始化数据工单数据
            initData(document,workRequestPdfDto,userInfo);

            // 关闭文档对象
            document.close();
        }catch(Exception e){
            throw new Exception("导出PDF失败!", e);
        }

    }

    /**
     * 初始化数据
     * @date 2020/5/8
     * @param document workRequestPdfDto
     * @return void
     */
    private void initData(Document document,WorkRequestPdfDto workRequestPdfDto,UserInfo userInfo) throws Exception{
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
       /* PdfFont fontBold  = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);*/
        Table table = new Table(new float[]{1, 3, 1, 2});
        table.setWidthPercent(100);
        table.setFixedLayout();
        /*table.setBorderLeft(new SolidBorder(Color.BLACK,2f));
        table.setBorderRight(new SolidBorder(Color.BLACK,2f));
        table.setBorderTop(new SolidBorder(Color.BLACK,2f));
        table.setBorderBottom(new SolidBorder(Color.BLACK,2f));*/
        //创建表头，一行四列
        Cell head = new Cell(1,4);
        //大标题, 颖网科技客户服务记录单
        Paragraph paragraph = new Paragraph("客户" + workRequestPdfDto.getWorkTypeName() + "服务记录单").
                setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(Head_Font_Size).setHeight(40);
        head.add(paragraph).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        //加入表格
        table.addHeaderCell(head);

        //创建表头，一行四列
        Cell head1=new Cell(1,2);
        //副标题
        Paragraph paragraph_1 = new Paragraph("委托单号：" + workRequestPdfDto.getCheckWorkCode()).
                setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(Text_Font_Size).setHeight(20);
        head1.add(paragraph_1).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);
        table.addHeaderCell(head1);

        Cell head2=new Cell(1,2);
        Paragraph paragraph_2 = new Paragraph("工单号：" + workRequestPdfDto.getWorkCode()).
                setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(Text_Font_Size).setHeight(20);
        head2.add(paragraph_2).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
        table.addHeaderCell(head2);

        //表格
        table.addCell(this.createLabelCell("客户名称", font));
        table.addCell(this.createValueCell(workRequestPdfDto.getCustomCorpName() +
                " " + StrUtil.trimToEmpty(workRequestPdfDto.getDeviceBranchName()), font));
        table.addCell(this.createLabelCell("联系人", font));
        table.addCell(this.createValueCell(workRequestPdfDto.getContactName() +
                " (" + workRequestPdfDto.getContactPhone() + "）", font));

        table.addCell(this.createLabelCell("详细地址", font, 40));
        table.addCell(this.createTextCell(workRequestPdfDto.getAddress(), font, 1, 1, 40));
        table.addCell(this.createLabelCell("设备类型", font, 40));
        table.addCell(this.createValueCell(workRequestPdfDto.getSmallClassName(), font, 1, 1, 40));

        table.addCell(this.createLabelCell("品牌型号", font));
        table.addCell(this.createValueCell(StrUtil.trimToEmpty(workRequestPdfDto.getBrandName()) +
                " " + StrUtil.trimToEmpty(workRequestPdfDto.getModelName()), font));
        table.addCell(this.createLabelCell("出厂序列号", font));
        table.addCell(this.createValueCell(workRequestPdfDto.getSerial(), font));

        table.addCell(this.createLabelCell("报修时间", font));
        table.addCell(this.createValueCell(DateUtil.format(workRequestPdfDto.getCreateTime(), "yyyy-MM-dd HH:mm"), font));
        table.addCell(this.createLabelCell("预约时间", font));
        table.addCell(this.createValueCell(DateUtil.format(workRequestPdfDto.getBookTimeEnd(), "yyyy-MM-dd HH:mm"), font));

        table.addCell(this.createLabelCell("报修内容", font, 180));
        table.addCell(this.createTextCell(workRequestPdfDto.getServiceRequest(), font, 1, 3, 80));

        table.addCell(this.createLabelCell("服务商", font));
        table.addCell(this.createValueCell("", font));
        table.addCell(this.createLabelCell("服务网点", font));
        table.addCell(this.createValueCell(workRequestPdfDto.getServiceBranchName(), font));

        table.addCell(this.createLabelCell("服务人员", font));
        String togetherEngineers = workRequestPdfDto.getTogetherEngineerList() == null ? "" : CollectionUtil.join(
                workRequestPdfDto.getTogetherEngineerList().stream().map(e -> e.getEngineerName())
                        .collect(Collectors.toList()), "，");
        String helpNames = StrUtil.trimToEmpty(workRequestPdfDto.getHelpNames()).replaceAll(",", "，");

        table.addCell(this.createValueCell(workRequestPdfDto.getEngineerName() +
                (StrUtil.isNotBlank(togetherEngineers) ? "，" + togetherEngineers : "") +
                (StrUtil.isNotBlank(helpNames) ? "，" + helpNames : "")
                , font, 1, 1, 20));

        table.addCell(this.createLabelCell("到达时间", font));
        table.addCell(this.createValueCell(DateUtil.format(workRequestPdfDto.getSignTime(), "yyyy-MM-dd HH:mm"), font));

        table.addCell(this.createLabelCell("开始时间", font));
        table.addCell(this.createValueCell(DateUtil.format(workRequestPdfDto.getStartTime(), "yyyy-MM-dd HH:mm"), font));
        table.addCell(this.createLabelCell("完成时间", font));
        table.addCell(this.createValueCell(DateUtil.format(workRequestPdfDto.getEndTime(), "yyyy-MM-dd HH:mm"), font));

        table.addCell(this.createLabelCell("完成情况", font, 120));
        table.addCell(this.createTextCell(workRequestPdfDto.getFinishDescription(), font, 1,3, 100));

        table.addCell(this.createLabelCell("客户评价", font, 30));
        table.addCell(this.createValueCell("□很满意 □满意 □一般 □不满意 □很不满意", font, 1, 1, 30));
        table.addCell(this.createLabelCell("客户签名", font, 30));

        Cell cell = new Cell();
        if (LongUtil.isNotZero(workRequestPdfDto.getSignature())) {
            try {
                //单元格插入图片
                ResponseEntity<byte[]> bytes = pdfUtil.fileFeignService.getImgByteArrayByFileId(
                        workRequestPdfDto.getSignature());
                byte[] imgIbytes = bytes.getBody();
                if (imgIbytes != null && imgIbytes.length > 0) {
                    Image img = new Image(ImageDataFactory.create(imgIbytes));
                    img.setHeight(30);
                    img.setWidth(60).setHorizontalAlignment(HorizontalAlignment.CENTER);
                    cell.add(img).setHorizontalAlignment(HorizontalAlignment.CENTER);
                }
                table.addCell(cell);
            } catch (Exception e) {
                table.addCell(this.createValueCell("", font));
            }
        } else {
            table.addCell(this.createValueCell("", font));
        }

        table.addCell(this.createLabelCell("备注", font, 80));
        table.addCell(this.createTextCell("", font, 1,3, 80));

//            // 文档插入绝对位置图片
//            Image img = new Image(ImageDataFactory.create(imgIbytes));
//            img.setFixedPosition(80, 560);
//            document.add(img);

        document.add(table);
    }

    /**
     * 生成委托商结算单Pdf
     *
     * @param response
     * @param settleDemanderDto
     */
    public void createSettleDemanderPdf(HttpServletResponse response, SettleDemanderDto settleDemanderDto) throws Exception {
        try{
            // 设置response参数，请求返回类型，可以打开下载页面
            response.reset();
            response.setContentType("application/pdf");
            String fileName = "结算单" + settleDemanderDto.getSettleCode();
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    java.net.URLEncoder.encode(fileName + ".pdf", "UTF-8"));
            //1、创建流对象
            PdfWriter pdfWriter=new PdfWriter(response.getOutputStream());

            //2、创建文档对象
            PdfDocument pdfDocument=new PdfDocument(pdfWriter);

            //3、创建内容文档对象
            Document document=new Document(pdfDocument, PageSize.A4);

            //4.初始化数据工单数据
            initSettleDemanderData(document, settleDemanderDto);

            // 关闭文档对象
            document.close();
        }catch(Exception e){
            throw new Exception("导出PDF失败!", e);
        }
    }

    /**
     * 初始化导出数据
     *
     * @param document
     * @param settleDemanderDto
     */
    public void initSettleDemanderData(Document document, SettleDemanderDto settleDemanderDto) throws Exception {
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        /* PdfFont fontBold  = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);*/
        Table table = new Table(new float[]{1, 2, 1, 2});
        table.setWidthPercent(100);
        table.setFixedLayout();
        /*table.setBorderLeft(new SolidBorder(Color.BLACK,2f));
        table.setBorderRight(new SolidBorder(Color.BLACK,2f));
        table.setBorderTop(new SolidBorder(Color.BLACK,2f));
        table.setBorderBottom(new SolidBorder(Color.BLACK,2f));*/
        //创建表头，一行四列
        Cell head = new Cell(2,4);
        //大标题, 颖网科技客户服务记录单
        Paragraph paragraph = new Paragraph(settleDemanderDto.getDemanderCorpName() + "服务情况统计表")
                .setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(Head_Font_Size).setHeight(40);
        head.add(paragraph).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        //加入表格
        table.addHeaderCell(head);

        Cell head1 = new Cell(2,2);
        Paragraph paragraph_1 = new Paragraph("结算单号：" + settleDemanderDto.getSettleCode()).
                setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(Text_Font_Size).setHeight(40);
        head1.add(paragraph_1).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);
        table.addHeaderCell(head1);

        Cell head2=new Cell(2,2);
        Paragraph paragraph_2 = new Paragraph("添加时间：" + DateUtil.format(settleDemanderDto.getOperateTime(), "yyyy-MM-dd")).
                setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(Text_Font_Size).setHeight(40);
        head2.add(paragraph_2).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
        table.addHeaderCell(head2);

        //表格
        table.addCell(this.createLabelCell("委托商", font, 40));
        table.addCell(this.createValueCell(settleDemanderDto.getDemanderCorpName(), font, 1, 1, 40)
                .setTextAlignment(TextAlignment.LEFT));

        table.addCell(this.createLabelCell("委托协议", font, 40));
        table.addCell(this.createValueCell(settleDemanderDto.getContNo(), font, 1, 1, 40)
                .setTextAlignment(TextAlignment.LEFT));

        table.addCell(this.createLabelCell("结算方式", font, 40));
        table.addCell(this.createValueCell(settleDemanderDto.getSettleWayName(), font, 1, 1, 40)
                .setTextAlignment(TextAlignment.LEFT));

        table.addCell(this.createLabelCell("结算周期", font, 40));
        if (settleDemanderDto.getStartDate() != null && settleDemanderDto.getEndDate() != null) {
            String periodString = DateUtil.format(settleDemanderDto.getStartDate(), "yyyy-MM-dd") +
                    DateUtil.format(settleDemanderDto.getEndDate(), "yyyy-MM-dd");
            table.addCell(this.createValueCell(periodString, font, 1, 1, 40)
                    .setTextAlignment(TextAlignment.LEFT));
        } else {
            table.addCell(this.createValueCell("", font, 1, 1, 40)
                    .setTextAlignment(TextAlignment.LEFT));
        }

        table.addCell(this.createLabelCell("工单总数", font, 40));
        table.addCell(this.createValueCell(String.valueOf(settleDemanderDto.getWorkQuantity()), font, 1, 1, 40)
                .setTextAlignment(TextAlignment.RIGHT));

        table.addCell(this.createLabelCell("总费用", font, 40));
        table.addCell(this.createValueCell("￥" + String.valueOf(settleDemanderDto.getSettleFee()), font,
                1, 1, 40).setTextAlignment(TextAlignment.RIGHT));

        table.addCell(this.createLabelCell("服务情况", font, 40));
        table.addCell(this.createValueCell("正常服务完成", font, 1, 3, 40)
                .setTextAlignment(TextAlignment.LEFT));

        table.addCell(this.createLabelCell("备注", font, 80));
        table.addCell(this.createValueCell(settleDemanderDto.getNote(), font, 1, 3, 80)
                .setTextAlignment(TextAlignment.LEFT));

        // 增加空行
        table.addCell(this.createValueCell("", font, 1, 4, 20).setBorder(Border.NO_BORDER));

        // 结算明细
        if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
            table.addCell(this.createLabelCell("对账单", font, 40));
            String verifyNames = "";
            if (CollectionUtil.isNotEmpty(settleDemanderDto.getWorkFeeVerifyDtoList())) {
                verifyNames = settleDemanderDto.getWorkFeeVerifyDtoList().stream().map(workFeeVerifyDto -> workFeeVerifyDto.getVerifyName())
                        .collect(Collectors.joining(", "));
            }
            table.addCell(this.createValueCell(verifyNames, font, 1, 3, 40)
                    .setTextAlignment(TextAlignment.LEFT));
        } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
            table.addCell(this.createLabelCell("结算工单", font, 40));
            String workCodes = "";
            if (CollectionUtil.isNotEmpty(settleDemanderDto.getWorkFeeDtoList())) {
                workCodes = settleDemanderDto.getWorkFeeDtoList().stream().map(workFeeDto -> workFeeDto.getWorkCode())
                        .collect(Collectors.joining(", "));
            }
            table.addCell(this.createValueCell(workCodes, font, 1, 3, 40)
                    .setTextAlignment(TextAlignment.LEFT));
        }

        // 增加空行
        table.addCell(this.createValueCell("", font, 1, 4, 40).setBorder(Border.NO_BORDER));

        Cell blank2=new Cell(2,2).setBorder(Border.NO_BORDER);
        table.addCell(blank2);

        Cell signCell = new Cell(2,2).add("签名：").setTextAlignment(TextAlignment.LEFT)
                .setFont(font).setFontSize(Text_Font_Size).setHeight(60).setBorder(Border.NO_BORDER);
        table.addCell(signCell);

        Cell blank3=new Cell(2,2).setBorder(Border.NO_BORDER);
        table.addCell(blank3);

        Cell dateCell = new Cell(2,2).add("日期：").setTextAlignment(TextAlignment.LEFT)
                .setFont(font).setFontSize(Text_Font_Size).setHeight(60).setBorder(Border.NO_BORDER);
        table.addCell(dateCell);

        document.add(table);
    }

    private Cell createLabelCell(String label, PdfFont font) {
        Cell cell = new Cell();
        cell.add(new Paragraph(label).setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFont(font).setFontSize(Text_Font_Size).setHeight(20));
        return cell;
    }

    private Cell createLabelCell(String label, PdfFont font, int height) {
        Cell cell = new Cell();
        cell.add(new Paragraph(label).setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFont(font).setFontSize(Text_Font_Size).setHeight(height));
        return cell;
    }

    private Cell createValueCell(String value, PdfFont font) {
        value = StrUtil.trimToEmpty(value);
        Cell cell = new Cell();
        cell.add(new Paragraph(value).setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE).setFont(font).setFontSize(Text_Font_Size)
                .setHeight(20));
        return cell;
    }

    private Cell createValueCell(String value, PdfFont font, int rowspan, int colspan, int height) {
        value = StrUtil.trimToEmpty(value);
        Cell cell = new Cell(rowspan, colspan);
        cell.add(new Paragraph(value).setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE).setFont(font).setFontSize(Text_Font_Size)
                .setHeight(height));
        return cell;
    }

    private Cell createTextCell(String value, PdfFont font, int rowspan, int colspan, int height) {
        value = StrUtil.trimToEmpty(value);
        Cell cell = new Cell(rowspan, colspan);
        Paragraph p = new Paragraph(value).setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE).setFont(font).setFontSize(Text_Font_Size)
                .setHeight(height);
        cell.add(p);
        return cell;
    }

}
