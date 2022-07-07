package Model_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DatabaseController {
    // 返回代码
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int ERROR = 2;
    public static final int NOT_FOUND = 3;
    public static final int DUPLICATE = 4;
    public static final int NOT_MATCH = 5;

    Connection connection;

    // ----- 数据库连接 -----
    // 建立数据库连接
    public int getConnection(String database, String user, String password, String port) {
        try {
            // 查找驱动程序类
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:" + port +
                            ";databaseName=" + database +
                            ";user=" + user +
                            ";password=" + password +
                            ";encrypt=false;trustServerCertificate=false;" +
                            "hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        System.out.println("Connected to database");
        return SUCCESS;
    }

    // 数据库是否连接成功
    public int isConnected() {
        try {
            if (connection == null || connection.isClosed()) {
                return FAIL;
            }
        } catch (Exception e) {
            return ERROR;
        }
        return SUCCESS;
    }

    // 数据库断开连接
    public int closeConnection() {
        try {
            if (isConnected() == SUCCESS) {
                connection.close();
            } else {
                return FAIL;
            }
        } catch (Exception e) {
            return ERROR;
        }
        System.out.println("Disconnected from database");
        return SUCCESS;
    }

    // ----- 数据库数据基本操作 -----
    // 初始化数据库表
    public int initializeDatabase() {
        String sql;
        try {
            // 创建一个表（若不存在），名为"Users"，用来存储用户信息
            // 用户唯一id（id）、用户名（username）、密码（password）、权限（privilege）（顾客、供应商和超级管理员）、
            // 创建时间（create_time）、上次登录时间（last_login_time）
            // language=sql
            sql = "IF OBJECT_ID('Users', 'U') IS NULL CREATE TABLE Users (" +
                    "id INT NOT NULL IDENTITY(1,1) PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "privilege VARCHAR(50) NOT NULL," +
                    "create_time DATETIME NOT NULL," +
                    ")";

            // 创建一个表（若不存在），名为"Goods"，用来存储商品属性
            // 商品唯一id（id）、商品名称（name）、商品进货价格（purchase_price）、商品出货价格(sale_price)、
            // 商品数量（count）、商品类型(type)
            //language=SQL
            sql += "IF OBJECT_ID('Goods', 'U') IS NULL CREATE TABLE Goods (" +
                    "id INT NOT NULL IDENTITY(1,1) PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "purchase_price FLOAT NOT NULL," +
                    "sale_price FLOAT NOT NULL," +
                    "count INT NOT NULL," +
                    "type VARCHAR(50) NOT NULL" +
                    ")";

            // 创建一个表（若不存在），名为"PurchaseOrder"，用来存储进货订单
            // 进货订单唯一id（id）、进货订单日期（date）、进货订单商品id（goods_id）、进货订单商品数量（count）、
            // 进货订单商品总价（total_price）、供应商id（supplier_id）
            //language=SQL
            sql += "IF OBJECT_ID('PurchaseOrder', 'U') IS NULL CREATE TABLE PurchaseOrder (" +
                    "id INT NOT NULL IDENTITY(1,1) PRIMARY KEY," +
                    "date DATETIME NOT NULL," +
                    "goods_id INT NOT NULL," +
                    "count INT NOT NULL," +
                    "total_price FLOAT NOT NULL," +
                    "supplier_id INT NOT NULL" +
                    ")";

            // 创建一个表（若不存在），名为"SaleOrder"，用来存储出货订单
            // 出货订单唯一id（id）、出货订单日期（date）、出货订单商品id（goods_id）、出货订单商品数量（count）、
            // 出货订单商品总价（total_price）、客户id（customer_id）
            //language=SQL
            sql += "IF OBJECT_ID('SaleOrder', 'U') IS NULL CREATE TABLE SaleOrder (" +
                    "id INT NOT NULL IDENTITY(1,1) PRIMARY KEY," +
                    "date DATETIME NOT NULL," +
                    "goods_id INT NOT NULL," +
                    "count INT NOT NULL," +
                    "total_price FLOAT NOT NULL," +
                    "customer_id INT NOT NULL" +
                    ")";

            // 创建用户名索引
            //language=SQL
            sql += "IF OBJECT_ID('idx_username', 'U') IS NULL " +
                    "CREATE INDEX idx_username ON Users(username)";
            // 创建商品名索引
            //language=SQL
            sql += "IF OBJECT_ID('idx_name', 'U') IS NULL " +
                    "CREATE INDEX idx_name ON Goods(name)";

            //language=SQL
            sql += "CREATE INDEX idx_name ON Goods(name)";

            // 进货表的goods_id是外键，引用商品表的id，进货表的supplier_id是外键，引用供应商表的id
            // 若以上外键已经存在，则不需要再次创建
            // 当主键被删除时，外键也会被删除
            //language=SQL
            sql += "IF OBJECT_ID('FK_PurchaseOrder_Goods', 'F') IS NULL " +
                    "ALTER TABLE PurchaseOrder ADD CONSTRAINT FK_PurchaseOrder_Goods " +
                    "FOREIGN KEY (goods_id) REFERENCES Goods(id) " +
                    "ON DELETE CASCADE ";
            //language=SQL
            sql += "IF OBJECT_ID('FK_PurchaseOrder_Supplier', 'F') IS NULL " +
                    "ALTER TABLE PurchaseOrder ADD CONSTRAINT FK_PurchaseOrder_Supplier " +
                    "FOREIGN KEY (supplier_id) REFERENCES Users(id) " +
                    "ON DELETE CASCADE ";
            // 出货表的goods_id是外键，引用商品表的id，出货表的customer_id是外键，引用客户表的id
            // 若以上外键已经存在，则不需要再次创建
            //language=SQL
            sql += "IF OBJECT_ID('FK_SaleOrder_Goods', 'F') IS NULL " +
                    "ALTER TABLE SaleOrder ADD CONSTRAINT FK_SaleOrder_Goods " +
                    "FOREIGN KEY (goods_id) REFERENCES Goods(id) " +
                    "ON DELETE CASCADE ";
            //language=SQL
            sql += "IF OBJECT_ID('FK_SaleOrder_Customer', 'F') IS NULL " +
                    "ALTER TABLE SaleOrder ADD CONSTRAINT FK_SaleOrder_Customer " +
                    "FOREIGN KEY (customer_id) REFERENCES Users(id) " +
                    "ON DELETE CASCADE ";

            // 执行sql
            connection.createStatement().execute(sql);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // 增加条目
    private int insertItem(String tableName, HashMap<String, String> map) {
        // INSERT INTO 表名（列名1，列名2，...，列名n）VALUES (值1，值2，...，值n)
        try {
            // 创建sql语句
            String sql = "INSERT INTO " + tableName + " (";
            String values = ") VALUES (";
            // 循环遍历map，拼接sql语句
            for (String key : map.keySet()) {
                sql += key + ",";
                values += "'" + map.get(key) + "',";
            }
            // 去掉最后一个逗号
            sql = sql.substring(0, sql.length() - 1);
            values = values.substring(0, values.length() - 1);
            // 拼接完整的sql语句
            sql += values + ")";

            System.out.println(sql);
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // 删除条目
    private int deleteItem(String tableName, String id) {
        // DELETE FROM 表名 WHERE id = 值
        try {
            String sql = "DELETE FROM " + tableName + " WHERE id = " + id;
            System.out.println(sql);
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // 获取条目
    private HashMap<String, Object> getItems(String tableName, String columns, String condition) {
        // SELECT (列名1，列名2，...，列名n) FROM 表名 WHERE 条件
        HashMap<String, Object> result = new HashMap<>();
        try {
            String sql = "SELECT " + columns + " FROM " + tableName;
            if (condition != null) {
                sql += " WHERE " + condition;
            }
            System.out.println(sql);
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            result.put("result", resultSet);
            result.put("returnCode", SUCCESS);
            return result;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("result", null);
            result.put("returnCode", ERROR);
            return result;
        }
    }

    // 更新条目
    private int updateItem(String tableName, HashMap<String, String> map, String condition) {
        // UPDATE 表名 SET 列名1 = 值1，列名2 = 值2，...，列名n = 值n WHERE 条件
        try {
            String sql = "UPDATE " + tableName + " SET ";
            for (String key : map.keySet()) {
                sql += key + " = " + map.get(key) + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            if (condition != null) {
                sql += " WHERE " + condition;
            }
            System.out.println(sql);
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    //开始事物
    private int beginTransaction() {
        String sql = "BEGIN TRANSACTION";
        try {
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // 提交事物
    private int commitTransaction() {
        String sql = "COMMIT";
        try {
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // 回滚事物
    private int rollbackTransaction() {
        String sql = "ROLLBACK";

        try {
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // 关闭ResultSet
    private int closeResultSet(ResultSet resultSet) {
        boolean result;
        try {
            resultSet.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    // ----- 杂项 -----

    // 获取当前系统时间
    public String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    // ----- 用户操作 -----
    // 注册用户
    public int registerUser(String username, String password, String privilege) {
        HashMap<String, Object> sqlResult = getItems("Users", "username", "username='" + username + "'");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS) {
            return ERROR;
        }

        // 检查用户名是否已存在
        ResultSet resultSet = (ResultSet) sqlResult.get("result");
        try {
            if (resultSet.next()) {
                System.out.println("Error: 数据库中查找不到该用户名");
                closeResultSet(resultSet);
                return DUPLICATE;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        } finally {
            closeResultSet(resultSet);
        }
        // 创建时间
        String createTime = getCurrentTime();
        // 插入用户信息
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        hashMap.put("privilege", privilege);
        hashMap.put("create_time", createTime);

        return insertItem("Users", hashMap);
    }

    // 登录用户
    public HashMap<String, Object> loginUser(String username, String password) {
        HashMap<String, Object> sqlResult = getItems(
                "Users", "username,password,privilege,create_time", "username='" + username + "'");
        HashMap<String, Object> result = new HashMap<>();
        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS) {
            result.put("returnCode", ERROR);
            return result;
        }

        ResultSet resultSet = (ResultSet) sqlResult.get("result");
        User user;

        try {
            // 检查用户名是否存在
            if (!resultSet.next()) {
                System.out.println("Error: The username is not in the database");
                result.put("returnCode", NOT_FOUND);
                return result;
            } else {
                // 检查密码是否正确
                if (!resultSet.getString("password").equals(password)) {
                    System.out.println("Error: 密码错误");
                    result.put("returnCode", NOT_MATCH);
                    return result;
                }
                // 返回登录结果
                user = new User(username, password, resultSet.getString("privilege"),
                        new Date(resultSet.getDate("create_time").getTime()));
                result.put("returnCode", SUCCESS);
                result.put("result", user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }

        return result;
    }

    // 更改密码
    public int changePassword(int id, String old_password, String new_password) {
        HashMap<String, Object> sqlResult = getItems("Users", "username, password", "id="+id);
        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS) {
            return ERROR;
        }
        ResultSet resultSet = (ResultSet) sqlResult.get("result");
        try {
            resultSet.next();
            // 检查密码是否正确
            if (!resultSet.getString("password").equals(old_password)) {
                System.out.println("Error: 密码错误");
                return NOT_MATCH;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        } finally {
            closeResultSet(resultSet);
        }
        // 更新密码
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("password", "'"+new_password+"'");
        return updateItem("Users", hashMap, "id=" + id);
    }

    // 获取用户信息
    public HashMap<String, Object> getUserInfo(int id) {
        HashMap<String, Object> sqlResult = getItems("Users", "*", "id='"+id+"'");
        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS) {
            return null;
        }
        ResultSet resultSet = (ResultSet) sqlResult.get("result");
        try {
            resultSet.next();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", resultSet.getInt("id"));
            hashMap.put("username", resultSet.getString("username"));
            hashMap.put("password", resultSet.getString("password"));
            hashMap.put("privilege", resultSet.getString("privilege"));
            hashMap.put("create_time", resultSet.getString("create_time"));
            return hashMap;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeResultSet(resultSet);
        }
    }

    // 获取所有用户
    public HashMap<String, Object> getAllUsers() {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("Users", "*", null);
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            result.put("returnCode", ERROR);
            return result;
        }

        int id;
        String username;
        String password;
        String privilege;
        String create_time;
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<Integer> idList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                username = resultSet.getString("username");
                password = resultSet.getString("password");
                privilege = resultSet.getString("privilege");
                userList.add(new User(username, password, privilege, null));
                idList.add(id);
            }
            result.put("returnCode", SUCCESS);
            result.put("result", userList);
            result.put("idList", idList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }
    // 通过用户名获取用户id
    public int getUserId(String username) {
        HashMap<String, Object> sqlResult = getItems("Users", "id", "username='"+username+"'");
        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS) {
            return -1;
        }
        ResultSet resultSet = (ResultSet) sqlResult.get("result");
        try {
            resultSet.next();
            return resultSet.getInt("id");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            closeResultSet(resultSet);
        }
    }

    // ----- 商品操作 -----
    // 添加商品到商品库
    public int addGoods(String name, float purchase_price, float sale_price, Integer stock, String type) {
        // 检查商品是否已在商品库中
        HashMap<String, Object> sqlResult = getItems("Goods", "name", "name='" + name + "'");
        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS) {
            return ERROR;
        }
        ResultSet resultSet = (ResultSet) sqlResult.get("result");
        try {
            if (resultSet.next()) {
                System.out.println("Error: 该商品已存在数据库");
                return DUPLICATE;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        } finally {
            closeResultSet(resultSet);
        }

        // insert the new goods into the database
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("purchase_price", Float.toString(purchase_price));
        hashMap.put("sale_price", Float.toString(sale_price));
        hashMap.put("count", stock.toString());
        hashMap.put("type", type);

        return insertItem("Goods", hashMap);
    }

    // 从商品库删除商品
    public int deleteGoods(String id) {
        return deleteItem("Goods", id);
    }

    // 修改商品信息
    // 选项为null时表示不修改
    public int updateGoods(int id, String name, Float purchase_price, Float sale_price, Integer count, String type) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (name != null) {
            hashMap.put("name", "'"+name+"'");
        }
        if (purchase_price != null) {
            hashMap.put("purchase_price", purchase_price.toString());
        }
        if (sale_price != null) {
            hashMap.put("sale_price", sale_price.toString());
        }
        if (count != null) {
            hashMap.put("count", count.toString());
        }
        if (type != null) {
            hashMap.put("type", "'"+type+"'");
        }
        return updateItem("Goods", hashMap, "id=" + id);
    }

    // 获取商品库中所有的商品
    public HashMap<String, Object> getAllGoods() {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("Goods", "*", null);
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            result.put("returnCode", ERROR);
            return result;
        }

        int id;
        //商品名称
        String name;
        float purchase_price;
        float sale_price;
        int count;
        String type;
        ArrayList<Goods> goodsList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                name = resultSet.getString("name");
                purchase_price = resultSet.getFloat("purchase_price");
                sale_price = resultSet.getFloat("sale_price");
                count = resultSet.getInt("count");
                type = resultSet.getString("type");
                goodsList.add(new Goods(id, name, purchase_price, sale_price, count, type));
            }
            result.put("returnCode", SUCCESS);
            result.put("result", goodsList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }

    // 通过id获取商品
    public HashMap<String, Object> getGoodsById(int id) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("Goods", "*", "id=" + id);
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            return null;
        }

        Goods goods = null;
        try {
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                float purchase_price = resultSet.getFloat("purchase_price");
                float sale_price = resultSet.getFloat("sale_price");
                int count = resultSet.getInt("count");
                String type = resultSet.getString("type");
                goods = new Goods(id, name, purchase_price, sale_price, count, type);
            }
            result.put("returnCode", SUCCESS);
            result.put("result", goods);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }

    // 通过名字获取商品
    public HashMap<String, Object> getGoodsByName(String name) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("Goods", "*", "name='" + name + "'");
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            result.put("returnCode", ERROR);
            return result;
        }

        Goods goods = null;
        try {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                float purchase_price = resultSet.getFloat("purchase_price");
                float sale_price = resultSet.getFloat("sale_price");
                int count = resultSet.getInt("count");
                String type = resultSet.getString("type");
                goods = new Goods(id, name, purchase_price, sale_price, count, type);
            }
            result.put("returnCode", SUCCESS);
            result.put("result", goods);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }

    // 通过类别获取商品
    public HashMap<String, Object> getGoodsByType(String type) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("Goods", "*", "type='" + type + "'");
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            result.put("returnCode", sqlResult.get("returnCode"));
            return result;
        }

        int id;
        String name;
        float purchase_price;
        float sale_price;
        int count;
        ArrayList<Goods> goodsList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                name = resultSet.getString("name");
                purchase_price = resultSet.getFloat("purchase_price");
                sale_price = resultSet.getFloat("sale_price");
                count = resultSet.getInt("count");
                goodsList.add(new Goods(id, name, purchase_price, sale_price, count, type));
            }
            result.put("returnCode", SUCCESS);
            result.put("result", goodsList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }

    // ----- 进货订单 -----
    // 新建进货订单
    // 进货表增加条目，库存表更改对应商品的库存数量（事务）
    public int newPurchaseOrder(int goods_id, int count, float total_price, int supplier_id) {
        // 开始事物
        beginTransaction();
        // 插入进货订单
        HashMap<String, String> hashMapPurchaseOrder = new HashMap<>();
        hashMapPurchaseOrder.put("goods_id", Integer.valueOf(goods_id).toString());
        hashMapPurchaseOrder.put("count", Integer.valueOf(count).toString());
        hashMapPurchaseOrder.put("total_price", Float.valueOf(total_price).toString());
        hashMapPurchaseOrder.put("supplier_id", Integer.valueOf(supplier_id).toString());
        hashMapPurchaseOrder.put("date", getCurrentTime());
        int result = insertItem("PurchaseOrder", hashMapPurchaseOrder);
        // 更改库存表
        HashMap<String, String> hashMapGoods = new HashMap<>();
        hashMapGoods.put("count", "count+" + count);
        if (result == SUCCESS) {
            result = updateItem("Goods", hashMapGoods, "id=" + goods_id);
        }
        // 提交事物
        if (result == SUCCESS) {
            commitTransaction();
        } else {
            rollbackTransaction();
        }
        return result;
    }

    // 获取所有进货订单
    public HashMap<String, Object> getAllPurchaseOrders() {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("PurchaseOrder", "*", null);
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            result.put("returnCode", sqlResult.get("returnCode"));
            return result;
        }

        int id;
        Date date;
        int _goods_id;
        String goods_id;
        int count;
        float total_price;
        int supplier_id;
        ArrayList<PurchaseOrder> purchaseOrderList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                date = new Date(resultSet.getDate("date").getTime());
                _goods_id = resultSet.getInt("goods_id");
                goods_id = ((Goods) getGoodsById(_goods_id).get("result")).getName();
                count = resultSet.getInt("count");
                total_price = resultSet.getFloat("total_price");
                supplier_id = resultSet.getInt("supplier_id");
                purchaseOrderList.add(new PurchaseOrder(id, date, goods_id, count, total_price, supplier_id));
            }
            result.put("returnCode", SUCCESS);
            result.put("result", purchaseOrderList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }

    // 删除进货订单
    public int deletePurchaseOrder(int id) {
        return deleteItem("PurchaseOrder", Integer.toString(id));
    }


    // ----- 出货订单 -----
    // 新建出货订单
    // 出货表增加条目，库存表更改对应商品的库存数量（事务）
    public int newSaleOrder(int goods_id, int count, float total_price, int customer_id) {
        // 开始事物
        beginTransaction();
        // 插入出货订单
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("goods_id", Integer.valueOf(goods_id).toString());
        hashMap.put("count", Integer.valueOf(count).toString());
        hashMap.put("total_price", Float.valueOf(total_price).toString());
        hashMap.put("customer_id", Integer.valueOf(customer_id).toString());
        hashMap.put("date", getCurrentTime());
        int result = insertItem("SaleOrder", hashMap);
        // 更改库存表
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("count", "count-" + count);
        if (result == SUCCESS) {
            result = updateItem("Goods", hashMap2, "id=" + goods_id);
        }
        // 提交事物
        if (result == SUCCESS) {
            commitTransaction();
        } else {
            rollbackTransaction();
        }
        return SUCCESS;
    }

    // 获取所有出货订单
    public HashMap<String, Object> getAllSaleOrders() {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> sqlResult = getItems("SaleOrder", "*", null);
        ResultSet resultSet = (ResultSet) sqlResult.get("result");

        // 检查SQL服务器返回结果
        if ((int) sqlResult.get("returnCode") != SUCCESS || resultSet == null) {
            result.put("returnCode", sqlResult.get("returnCode"));
            return result;
        }

        int id;
        Date date;
        int _goods_id;
        String goods_id;
        int count;
        float total_price;
        int customer_id;
        ArrayList<SaleOrder> sellOrderList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                date = new Date(resultSet.getDate("date").getTime());
                _goods_id = resultSet.getInt("goods_id");
                goods_id = ((Goods) getGoodsById(_goods_id).get("result")).getName();
                count = resultSet.getInt("count");
                total_price = resultSet.getFloat("total_price");
                customer_id = resultSet.getInt("customer_id");
                sellOrderList.add(new SaleOrder(id, date, goods_id, count, total_price, customer_id));
            }
            result.put("returnCode", SUCCESS);
            result.put("result", sellOrderList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            result.put("returnCode", ERROR);
            return result;
        } finally {
            closeResultSet(resultSet);
        }
        return result;
    }

    // 删除出货订单
    public int deleteSaleOrder(int id) {
        return deleteItem("SaleOrder", Integer.toString(id));
    }
}
