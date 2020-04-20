package com.rainwood.chestnut.request;

import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.okhttp.OkHttp;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.okhttp.RequestParams;
import com.rainwood.chestnut.utils.DeviceIdUtils;

import java.io.File;

/**
 * @Author: sxs
 * @Time: 2020/3/28 14:56
 * @Desc: 接口请求API
 */
public final class RequestPost {

    /**
     * 获取商家列表
     *
     * @param keyWord  搜索关键字
     * @param listener
     */
    public static void getStoreList(String keyWord, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("keyWord", keyWord);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getMyStorelist", params, listener);
    }

    /**
     * 获取商家口令
     *
     * @param id
     * @param listener
     */
    public static void getStoreCode(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getStoreCode", params, listener);
    }

    /**
     * 添加商家
     *
     * @param id       商家编号
     * @param code     商家口令
     * @param listener
     */
    public static void joinStore(String id, String code, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("code", code);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=joinStore", params, listener);
    }

    /**
     * 个人中心
     *
     * @param listener
     */
    public static void getPerson(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getPerson", params, listener);
    }

    /**
     * 获取消息列表
     *
     * @param listener
     */
    public static void getMessagelist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getMessagelist", params, listener);
    }

    /**
     * 查看消息详情
     *
     * @param id
     * @param listener
     */
    public static void getMessageDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getMessageInfo", params, listener);
    }

    /**
     * 获取验证码
     *
     * @param tel      手机号码
     * @param type     忘记密码时传pwd， 注册时传 register ,其他则不传
     * @param listener
     */
    public static void getVerifyCode(String tel, String type, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        params.add("type", type);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientLogin.php?type=getCode", params, listener);
    }

    /**
     * 修改手机号
     *
     * @param tel      手机号码
     * @param prove    验证码
     * @param listener
     */
    public static void modifyTel(String tel, String prove, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        params.add("prove", prove);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=changeTel", params, listener);
    }

    /**
     * 重置密码
     * @param newPas
     * @param surePas
     * @param listener
     */
    public static void resetPwd(String newPas, String surePas, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("newPas", newPas);
        params.add("surePas", surePas);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientLogin.php?type=changePwd", params, listener);
    }

    /**
     * 比较验证码
     * @param tel
     * @param prove
     * @param listener
     */
    public static void compareCode(String tel, String prove, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("tel", tel);
        params.add("prove", prove);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientLogin.php?type=compareCode", params, listener);
    }

    /**
     * 修改密码
     * @param contactTel 电话号码
     * @param prove 验证码
     * @param newPas 新密码
     * @param surePas 确认密码
     * @param listener
     */
    public static void changePassword(String contactTel , String prove, String newPas, String surePas, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("contactTel", contactTel);
        params.add("prove", prove);
        params.add("newPas", newPas);
        params.add("surePas", surePas);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=changePassword", params, listener);
    }

    /**
     * 获取收货地址列表
     *
     * @param listener
     */
    public static void getClientAddresslist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getAddresslist", params, listener);
    }

    /**
     * 设置默认地址
     *
     * @param id
     * @param listener
     */
    public static void setDefaultAddress(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=setDefaultAddress", params, listener);
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @param listener
     */
    public static void delAddress(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=delAddress", params, listener);
    }

    /**
     * 客户收货地址详情
     *
     * @param id
     * @param listener
     */
    public static void getAddressDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getAddressInfo", params, listener);
    }

    /**
     * 退出登录
     *
     * @param listener
     */
    public static void loginOut(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=loginOut", params, listener);
    }

    /**
     * 获取购物车列表
     * @param storeId 门店ID
     * @param listener
     */
    public static void getCartlist(String storeId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("storeId", storeId);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getCartlist", params, listener);
    }

    /**
     * 客户订单列表
     * @param storeId 门店id
     * @param workFlow waitSend:待发货 waitRec 待付款 waitSelf 待自提 complete 已完成
     * @param keyWord 搜索关键字
     * @param page 分页码
     * @param listener
     */
    public static void getOrderList(String page, String storeId, String workFlow, String keyWord, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("storeId", storeId);
        params.add("page", page);
        params.add("workFlow", workFlow);
        params.add("keyWord", keyWord);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getAllOrder", params, listener);
    }

    /**
     * 查看订单详情
     * @param id 订单id
     * @param listener
     */
    public static void getOrderDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getOrderInfo", params, listener);
    }

    /**
     * 点击商家进入到详情页面时
     * @param id
     * @param listener
     */
    public static void enterStoreDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=enterStoreInfo", params, listener);
    }

    /**
     * 进入商家页面时获取的商家的信息
     * @param type classify/分类，promotion/促销，new/新品
     * @param listener
     */
    public static void getStoreInfo(String type, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("type", type);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getGoodslist", params, listener);
    }

    /**
     * 点击一级分类，获取二级分类
     * @param id
     * @param listener
     */
    public static void getClassify(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getClassify", params, listener);
    }

    /**
     * 商品详情页
     * @param id    商品id
     * @param listener
     */
    public static void getGoodsDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getGoodsInfo", params, listener);
    }

    /**
     * 客户下单页
     * @param goodsId 商品id
     * @param storeId 门店id
     * @param listener
     */
    public static void getConfirmOrderPage(String goodsId, String storeId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("goodsId", goodsId);
        params.add("storeId", storeId);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getConfirmOrderPage", params, listener);
    }

    /**
     * 购物车数量加减和编辑
     * @param type
     * @param id
     * @param num
     * @param listener
     */
    public static void goodsCartNumEdit(String type, String id, String num, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("type", type);
        params.add("num", num);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=goodsCartNumEdit", params, listener);
    }

    /**
     * 提交订单
     * @param goodsId 商品id
     * @param storeId 门店id
     * @param addressId 地址id
     * @param freightType 1:配送 2：自提
     * @param listener
     */
    public static void commitOrder(String goodsId, String storeId, String addressId, String freightType, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("goodsId", goodsId);
        params.add("storeId", storeId);
        params.add("addressId", addressId);
        params.add("freightType", freightType);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=commitOrder", params, listener);
    }

    /**
     * 提交退货
     * @param id 订单商品明细id
     * @param text 退货原因
     * @param listener
     */
    public static void commitRefundOrder(String id, String text, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("text", text);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=commitRefundOrder", params, listener);
    }

    /**
     * 取消退货
     * @param id
     * @param listener
     */
    public static void cancelRefund(String id,OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=cancelRefund", params, listener);
    }

    /**
     * 获取退货页面的数据
     * @param id
     * @param listener
     */
    public static void getRefundPage(String id,OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getRefundPage", params, listener);
    }

    // 商品

    /**
     * 分类下商品列表
     * @param id 二级分类id
     * @param listener
     */
    public static void getClassifyGoods(String id, String page, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("page", page);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getClassifyGoods", params, listener);
    }

    // 地址更新及编辑
    public static void addressEdit(String id, String companyName, String contactName, String contactTel,
                                   String region, String addressMx, String isDefault, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("companyName", companyName);
        params.add("contactName", contactName);
        params.add("contactTel", contactTel);
        params.add("region", region);
        params.add("addressMx", addressMx);
        params.add("isDefault", isDefault);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=addressEdit", params, listener);
    }

    /**
     * 获取国家地区列表
     * @param listener
     */
    public static void getRegion(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getRegion", params, listener);
    }

    // 个人中心头像修改
    public static void changeHeadImg(File img, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("img", img);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=changeTouxiang", params, listener);
    }

    // 版本升级
    public static void  VerisonUpdate(String version, String imei, String url, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("version", version);
        params.add("imei", imei);
        params.add("url", url);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientLogin.php?type=appVersionEdit", params, listener);
    }

    /**
     * 注册
      * @param prove 验证码
     * @param tel 电话
     * @param pwd 密码
     * @param listener
     */
    public static void registerUser(String prove, String tel, String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        params.add("prove", prove);
        params.add("pwd", pwd);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientLogin.php?type=registerUser", params, listener);
    }

    /**
     * 登录
     * @param tel 电话
     * @param pwd 密码
     * @param listener
     */
    public static void login(String tel, String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        params.add("pwd", pwd);
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientLogin.php?type=login", params, listener);
    }

    /**
     * 商品详情页添加到购物车
     * @param type
     * @param skuId
     * @param num
     * @param listener
     */
    public static void addInCart(String type, String skuId, String num, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("type", type);
        params.add("skuId", skuId);
        params.add("num", num);
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=addInCart", params, listener);
    }
}
