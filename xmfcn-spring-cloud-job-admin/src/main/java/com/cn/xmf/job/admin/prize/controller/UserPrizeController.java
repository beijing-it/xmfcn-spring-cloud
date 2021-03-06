package com.cn.xmf.job.admin.prize.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.prize.rpc.UserPrizeService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.wx.UserPrize;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * UserPrizeController(奖品信息)
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@Controller
@RequestMapping("/userPrize")
@SuppressWarnings("all")
public class UserPrizeController {

    private static Logger logger = LoggerFactory.getLogger(UserPrizeController.class);
    @Autowired
    private UserPrizeService userPrizeService;

    @RequestMapping
    public String index() {
        return "prize/userPrize-index";
    }

    /**
     * getList:(获取奖品信息分页查询接口)
     *
     * @param request
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("pageList")
    @ResponseBody
    public JSONObject getList(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String startStr = request.getParameter("start");
        String length = request.getParameter("length");
        int pageSize = 10;
        int pageNo = 1;
        int start = 0;
        if (StringUtil.isNotBlank(startStr)) {
            start = StringUtil.stringToInt(startStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        if (start > 0) {
            pageNo = (start / pageSize) + 1;
        }
        param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取奖品信息分页查询接口) 开始  param={}", param);

        param.put("id", id);
        param.put("type", type);
        param.put("name", name);
        Partion pt = userPrizeService.getList(param);
        List<UserPrize> list = null;
        int totalCount = 0;
        //long totalCount = 0;
        if (pt != null) {
            list = (List<UserPrize>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        retJon.put("data", list);
        retJon.put("recordsTotal", totalCount);
        retJon.put("recordsFiltered", totalCount);
        logger.info("getList:(获取奖品信息分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除奖品信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "删除失败");
        String ids = null;
        ids = request.getParameter("id");
        int id = StringUtil.stringToInt(ids);
        logger.info("delete 开始============>" + id);
        if (id <= 0) {
            returnT.setMsg("参数错误");
            return returnT;
        }
        long newId = id;
        boolean ret = userPrizeService.delete(newId);
        if (ret) {
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * save:(保存奖品信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(UserPrize userPrize) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = JSON.toJSONString(userPrize);
        logger.info("save:(保存奖品信息数据接口) 开始  parms={}", parms);
        if (userPrize == null) {
            returnT.setMsg("参数为空");
            return returnT;
        }
        if (userPrize == null) {
            returnT.setMsg("参数错误");
            return returnT;
        }
        UserPrize ret = userPrizeService.save(userPrize);
        if (ret == null) {
            returnT.setMsg("保存数据失败");
            return returnT;
        }
        returnT.setCode(ReturnT.SUCCESS_CODE);
        returnT.setMsg("成功");
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

}