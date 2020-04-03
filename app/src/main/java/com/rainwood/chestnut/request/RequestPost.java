package com.rainwood.chestnut.request;

import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.okhttp.OkHttp;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.okhttp.RequestParams;

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
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=joinStore", params, listener);
    }

    /**
     * 个人中心
     *
     * @param listener
     */
    public static void getPerson(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getPerson", params, listener);
    }

    /**
     * 获取消息列表
     *
     * @param listener
     */
    public static void getMessagelist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
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
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=changeTel", params, listener);
    }

    /**
     * 获取收货地址列表
     *
     * @param listener
     */
    public static void getClientAddresslist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
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
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientPerson.php?type=getAddressInfo", params, listener);
    }

    /**
     * 退出登录
     *
     * @param listener
     */
    public static void loginOut(OnHttpListener listener) {
        RequestParams params = new RequestParams();
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
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getCartlist", params, listener);
    }

    /**
     * 获取订单列表
     * @param storeId 选择的门店id
     * @param listener
     */
    public static void getOrderList(String storeId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("storeId", storeId);
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
        OkHttp.post(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getOrderInfo", params, listener);
    }
}
