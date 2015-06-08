package com.lsh.wxweb.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.lsh.wxweb.common.IpUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Luoshuhong
 * @Company  
 * 2015年6月6日
 *
 */
public class BasicAction extends ActionSupport {
    
    private static final long serialVersionUID = 1L;

    public HttpServletRequest getRequest() {
        // 获得request
        return ServletActionContext.getRequest();

    }

    public HttpServletResponse getResponse() {
        // 获得response
        return ServletActionContext.getResponse();
    }

    public String getRealIp() {
        // 获取ip
        return IpUtil.getRealIp(getRequest());
    }

    public HttpSession getSession() {
        // 获取session
        return getRequest().getSession();
    }

}
