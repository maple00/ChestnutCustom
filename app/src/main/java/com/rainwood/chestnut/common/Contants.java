package com.rainwood.chestnut.common;

/**
 * @Author: shearson
 * @Time: 2020/3/1 17:41
 * @Desc: 常量变量记录 ---
 */
public final class Contants {

    /**
     * 记录最新的填写的验证码手机号
     */
    public static String telNum;

    /**
     * 记录加载
     * 0x101: 购物车的商品
     * 0x102: 确认订单
     * 0x103; 订单详情
     * 0x104: 编辑收货地址
     * 0x105: 新增收货地址
     * 0x106: 确认订单选择收货地址
     * 0x107: 订单搜索
     */
    public static int RECORD_POS;

    /**
     * 收货地址选择
     * 0x101: 购物车选择收货地址
     */
    public static int ADDRESS_POS_SIZE;
    /**
     * 商品状态
     * 0x101: 可退货
     * 0x102: 退货中
     */
    public static int GOODS_REFUNDS_SIZE;

    /**
     * 确认订单选择地址 falg
     */
    public static final int ADDRESS_REQUEST = 0x1124;

    /**
     * fragment 跳转记录
     */
    public static final int HOME_SIZE = 0x1001;
    public static final int GOODS_SIZE = 0x1002;
    public static final int CART_SIZE = 0x1003;
    public static final int ORDER_SIZE = 0x1004;
    public static final int PERSONAL_SIZE = 0x1005;

    /**
     * 网络请求失败
     */
    public static final String HTTP_MSG_RESPONSE_FAILED = "The request data failed and the response code is not 200,code = ";

    /**
     * ROOT_URL
     */
    public static final String ROOT_URL = "https://www.yumukeji.cn/project/oushang/";

    /**
     * 初始化生成token
     */
    public static String token;

    /**
     * request code
     */
    // 订单请求码
    public static final int ORDER_REQUEST_SIZE = 0x2001;
    // 可退货request
    public static final int REFUNDS_REQUEST = 0x2002;
    // 退货中
    public static final int REFUNDS_APPLYING = 0x2003;

}
