package com.zjft.usp.msg.helper;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjft.usp.msg.model.Sms;
import com.zjft.usp.msg.model.SmsConfig;
import com.zjft.usp.msg.model.SmsResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author : dcyu
 * @Date : 2019年8月15日
 * @Desc : 短信发送帮助类
 */
@Slf4j
public class SmsSendHelper {



    @Value("${startCode: 0}")
    private int startCode;

    private SmsConfig config;

    private Map<Integer, String> appTypeMap;

    private Map<Integer, String> smsTypeMap;

    public SmsSendHelper(SmsConfig config, Map<Integer, String> appTypeMap, Map<Integer, String> smsTypeMap){
        this.config = config;
        this.appTypeMap = appTypeMap;
        this.smsTypeMap = smsTypeMap;
    }

    /**
     * 加载属性
     * @return
     */
    private DefaultProfile getDefaultProfile(){
        return DefaultProfile.getProfile(this.config.getRegionId(), this.config.getAccessKeyId(),this.config.getAccessKeySecret());
    }

    /**
     * 发送相同内容短信给单人或多人，多人手机号码之间用英文(,)分隔
     * @param sms : 短信实体
     * @return
     */
    public SmsResult sendSms(Sms sms){
        try{
            if(this.config == null){
                return SmsResult.info("短信服务配置为空");
            }
            //检查Sms 对象属性
            String msg = checkSms(sms);
            if(!StringUtils.isEmpty(msg)){
                return SmsResult.info(msg);
            }
            //系统定义应用类型转换为签名，短信类型转换为模板编号
            if(this.appTypeMap == null){
                return SmsResult.info("应用类型映射为NULL");
            }
            String afterAppType = this.appTypeMap.get(sms.getAppId());
            if(StringUtils.isEmpty(afterAppType)){
                return SmsResult.info("应用类型不存在");
            }
            if(this.smsTypeMap == null){
                return SmsResult.info("短信类型映射为NULL");
            }
            String templateCode = this.smsTypeMap.get(sms.getSmsType());
            if(StringUtils.isEmpty(templateCode)){
                return SmsResult.info("模板编号");
            }
            IAcsClient client = new DefaultAcsClient(this.getDefaultProfile());
            //请求参数
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", this.config.getRegionId());
            request.putQueryParameter("PhoneNumbers", sms.getPhoneNumbers());
            request.putQueryParameter("SignName", afterAppType);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", sms.getParam());
            log.info("短信发送开始");
            CommonResponse response = client.getCommonResponse(request);
            log.info("短信发送结束");
            String data = response.getData();
            log.info("短信发送第三方平台返回数据：" + data);
            JsonObject responseJson = JsonParser.class.newInstance().parse(response.getData()).getAsJsonObject();
            /*状态码*/
            String returnCode = responseJson.get("Code").getAsString();
            /*状态码描述*/
            String returnMessage = responseJson.get("Message").getAsString();
            /*回执ID*/
            String bizId = responseJson.get("BizId").getAsString();
            /*请求ID*/
            String requestId = responseJson.get("RequestId").getAsString();
            //通过returnCode判断是否发送给第三方成功
            if(!"OK".equals(returnCode)){
                log.error("短信发送调用第三方接口失败", returnMessage);
                return SmsResult.fail(returnMessage, bizId, requestId);
            }
            return SmsResult.success(bizId,requestId);
        }catch(ServerException e){
            log.error("短信发送服务器异常", e.getMessage());
            return SmsResult.fail("短信发送服务器异常");
        }catch(ClientException e){
            log.error("短信发送客户端异常", e.getMessage());
            return SmsResult.fail("短信发送客户端异常");
        }catch(Exception e){
            log.error("短信发送其他异常", e.getMessage());
            return SmsResult.fail("短信发送异常");
        }
    }


    /**
     * 检查短信对象的合法性
     * @param sms
     * @return
     */
    public static String checkSms(Sms sms){
        //手机号码
        if(StringUtils.isEmpty(sms.getPhoneNumbers())){
            return "手机号码为空！";
        }else{
            //检查号码格式
            String regEx = "^\\d{11}$";
            String[] phoneNumbers = sms.getPhoneNumbers().split(",");
            for(String phoneNumber : phoneNumbers){
                Boolean ifMatch = Pattern.matches(regEx, phoneNumber);
                if(!ifMatch){
                    return "手机号码格式不符合！";
                }
            }
        }
        //应用编号
        if(StringUtils.isEmpty(sms.getAppId())){
            return "未指定应用！";
        }
        //短信类型 根据短信类型判断是否需要增加参数校验
        if(StringUtils.isEmpty(sms.getSmsType())){
            return "未指定短信发送类型！";
        }
        return "";
    }
}
