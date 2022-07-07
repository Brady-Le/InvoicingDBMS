package pageExample;/*
黑盒测试
测试数据库连接模块，业务模块
*/

import Model_DB.DatabaseController;
import Model_DB.Goods;
import Model_DB.PurchaseOrder;
import Model_DB.SaleOrder;

import java.util.ArrayList;

public class Test {
    static final String DATABASE = "purchase";
    static final String SERVER_PORT = "1433";
    static final String USERNAME = "sa";
    static final String PASSWORD = "123456";

    static DatabaseController db;

    public static void main(String[] args) {
        // 实例化数据库控制器
        db = new DatabaseController();
        // 连接数据库
        System.out.println("连接数据库 - 0");
        System.out.println(db.getConnection(DATABASE, USERNAME, PASSWORD, SERVER_PORT));
        // 数据库是否连接成功
        System.out.println("数据库是否连接成功 - 0");
        System.out.println(db.isConnected());
        // 数据表初始化
        System.out.println("数据表初始化 - 0");
        System.out.println(db.initializeDatabase());
        // 注册新用户
        System.out.println("注册用户 - 0");
        System.out.println(db.registerUser("admin", "admin", "admin"));
        // 重复注册
        System.out.println("重复注册 - 4");
        System.out.println(db.registerUser("admin", "admin", "admin"));
        // 登录已有用户
        System.out.println("登录已有用户 - 0");
        System.out.println(db.loginUser("admin", "admin"));
        // 登录未有用户
        System.out.println("登录未有用户 - 3");
        System.out.println(db.loginUser("wrong_user", "password"));
        // 登录密码错误
        System.out.println("登录密码错误 - 5");
        System.out.println(db.loginUser("admin", "wrong_pw"));
        // 修改密码
        System.out.println("修改密码 - 0");
        System.out.println(db.changePassword(1, "admin", "new_password"));
        // 修改密码错误
        System.out.println("修改密码错误 - 5");
        System.out.println(db.changePassword(1, "admin", "wrong_password"));
        // 添加商品
        System.out.println("添加商品 - 0");
        System.out.println(db.addGoods("apple", 1.0f, 2.0f, 114514, "fruit"));
        // 重复添加商品
        System.out.println("重复添加商品 - 4");
        System.out.println(db.addGoods("apple", 1.0f, 2.0f, 114514, "fruit"));
        // 通过id查询商品
        System.out.println("通过id查询商品 - apple");
        System.out.println(((Goods) db.getGoodsById(1).get("result")).getName());
        // 通过name查询商品
        System.out.println("通过name查询商品 - apple");
        System.out.println(((Goods )db.getGoodsByName("apple").get("result")).getName());
        // 通过type查询商品
        System.out.println("通过type查询商品 - apple banana");
        db.addGoods("banana", 1.0f, 2.0f, 1919810, "fruit");
        db.addGoods("candy", 1.0f, 2.0f, 15, "food");
        ArrayList<Goods> goods = (ArrayList<Goods>) db.getGoodsByType("fruit").get("result");
        for (Goods g : goods) {
            System.out.println(g.getName());
        }
        // 查询所有商品
        System.out.println("查询所有商品 - apple banana candy");
        goods = (ArrayList<Goods>) db.getAllGoods().get("result");
        for (Goods g : goods) {
            System.out.println(g.getName() + " " + g.getCount());
        }
        // 更新商品
        System.out.println("更新商品 - phone");
        db.updateGoods(1, "apple", 1.0f, 2.0f, 114514, "phone");
        System.out.println(((Goods) db.getGoodsById(1).get("result")).getType());
        // 新增进货单并显示所有进货单
        System.out.println("新增进货单 - 1 2");
        db.newPurchaseOrder(1, 2, 2, 1);
        ArrayList<PurchaseOrder> purchaseOrders = (ArrayList<PurchaseOrder>) db.getAllPurchaseOrders().get("result");
        for (PurchaseOrder po : purchaseOrders) {
            System.out.println(po.getGoods_id() + " " + po.getCount());
        }
        // 新增出货单并显示所有出货单
        System.out.println("新增出货单 - 2 3");
        db.newSaleOrder(2, 3, 2, 1);
        ArrayList<SaleOrder> saleOrders = (ArrayList<SaleOrder>) db.getAllSaleOrders().get("result");
        for (SaleOrder so : saleOrders) {
            System.out.println(so.getGoods_id() + " " + so.getCount());
        }
        // 查询所有商品
        System.out.println("查询所有商品 - apple banana candy");
        goods = (ArrayList<Goods>) db.getAllGoods().get("result");
        for (Goods g : goods) {
            System.out.println(g.getName()+" "+g.getCount());
        }
        //获取所有出货订单的result
        ArrayList<SaleOrder> saleOrders1 = (ArrayList<SaleOrder>) db.getAllSaleOrders().get("result");
        //获取所有出货订单的goods_id
        ArrayList<Integer> goods_id = (ArrayList<Integer>) db.getAllSaleOrders().get("goods_id");
        System.out.println("goods_id");
        System.out.println(goods_id);
        //获取所有出货订单的商品名称
        ArrayList<String> goods_name = (ArrayList<String>) db.getAllSaleOrders().get("goods_name");
        System.out.println("goods_name");
        System.out.println(goods_name);

        // 断开数据库连接
        System.out.println("断开数据库连接 - 0");
        System.out.println(db.closeConnection());
        System.out.println("测试完毕");
    }
}
