# 数据库文档

## 数据库属性

基于Micrsoft SQL Server 2019开发

数据库名：<未定>

## 数据库结构

### 表属性

用户表，表名：Users

| 键名              | 类型       | 属性       | 解释        |
|-----------------|----------|----------|-----------|
| id              | 整数       | 非空 自增 主键 | 用户在数据库的id |
| username        | 字符串      | 非空       | 用户名       |
| password        | 字符串      | 非空       | 用MD5加密的密码 |
| privilege       | 字符串      | 非空       | 权限        |
| create_time     | DATETIME | 非空       | 账户创建时间    |
| last_login_time | DATETIME |          | 上次登录时间    |


商品表，表名：Goods

| 键名             | 类型  | 属性       | 解释        |
|----------------|-----|----------|-----------|
| id             | 整数  | 非空 自增 主键 | 商品在数据库的id |
| name           | 字符串 | 非空       | 商品名       |
| purchase_price | 浮点数 | 非空       | 商品入货单价    |
| sale_price     | 浮点数 | 非空       | 商品出货单价    |
| count          | 整数  | 非空       | 商品数量      |
| type           | 字符串 | 非空       | 商品类型      |

入货表，表名：PurchaseOrder

| 键名          | 类型       | 属性       | 解释         |
|-------------|----------|----------|------------|
| id          | 整数       | 非空 自增 主键 | 入货单在数据库的id |
| date        | DATETIME | 非空       | 入货时间       |
| goods_id    | 整数       | 非空       | 商品在商品表的id  |
| count       | 整数       | 非空       | 入货数量       |
| total_price | 浮点数      | 非空       | 总价         |
| supplier_id | 整数       | 非空       | 供货商在用户表的id |

出货表，表名：SaleOrder

| 键名          | 类型       | 属性       | 解释         |
|-------------|----------|----------|------------|
| id          | 整数       | 非空 自增 主键 | 出货单在数据库的id |
| date        | DATETIME | 非空       | 入货时间       |
| goods_id    | 整数       | 非空       | 商品在商品表的id  |
| count       | 整数       | 非空       | 入货数量       |
| total_price | 浮点数      | 非空       | 总价         |
| customer_id | 整数       | 非空       | 顾客在用户表的id  |

### 外键

| 外键名                       | 目标表           | 目标键         | 来源表      | 来源键 |
|---------------------------|---------------|-------------|----------|-----|
| FK_PurchaseOrder_Goods    | PurchaseOrder | goods_id    | Goods    | id  |
| FK_PurchaseOrder_Supplier | PurchaseOrder | supplier_id | Supplier | id  |
| FK_SaleOrder_Goods        | SaleOrder     | goods_id    | Goods    | id  |
| FK_SaleOrder_Customer     | SaleOrder     | customer_id | Customer | id  |

## DatabaseController 数据库基本功能实现类

用来直接连接数据库并构造SQL语句。

### 状态码

方法返回状态码，具体如下：

* `0`: 成功
* `1`: 失败
* `2`: 发生错误
* `3`: 找不到结果
* `4`: 结果重复
* `5`: 结果不匹配

### 数据库方法

#### getConnection

``` Java
public int getConnection(String database, String user, String password, String port)
```

建立数据库连接

输入：数据库名，用户名，密码，端口号

返回：状态码

#### isConnected

``` Java
public int isConnected()
```

检查数据库连接是否成功

返回：状态码

#### closeConnection

    
``` Java
public int closeConnection()
```

关闭数据库连接

返回：状态码

### 数据库数据基本操作方法

#### initializeDatabase

``` Java
public int initalizeDatabase()
```

初始化数据库表，包括创建数据表、外键。

返回：状态码

#### insertItem

``` Java
private int insertItem(String name, HashMap<String, String> map)
```

插入数据项到数据表

输入：数据表名，数据键值表

返回：状态码

#### deleteItem

``` Java
private int deleteItem(String tableName, String id)
```

从数据表删除数据项

输入：数据表名，数据项id

返回：状态码

#### getItems

``` Java
private HashMap<String, Object> getItems(String tableName, String columns, String condition)
```

从数据表获取数据项

输入：数据表名，列，条件

返回：数据项键值表，包括(int) returnCode和(ResultSet) result

#### updateItem

``` Java
private int updateItem(String tableName, HashMap<String, Object> map, String condition) 
```

更新选定数据表的数据项

输入：数据表名，列，条件

返回：状态码

#### beginTransaction

``` Java
private void beginTransaction()
```

开始数据库事务

返回：状态码

#### commitTransaction

``` Java
private int commitTransaction()
```

提交数据库事务

返回：状态码

#### rollbackTransaction

``` Java
private int rollbackTransaction()
```

回滚数据库事务

返回：状态码

#### closeResultSet

``` Java
private int closeResultSet(ResultSet resultSet)
```

关闭数据库结果集

输入：数据库结果集

返回：状态码

### 业务方法

#### registerUser

``` Java
public int registerUser(String username, String password, String privilege)
```

注册用户

输入：用户名，密码，权限

返回：状态码

#### loginUser

``` Java
public int loginUser(String username, String password)
```

登录用户

输入：用户名，密码

返回：状态码

#### addGoods

``` Java
public int addGoods(String name, String price, Integer stock)
```

添加商品到商品库

输入：商品名，商品价格，商品库存

返回：状态码

#### deleteGoods

``` Java
public int deleteGoods(String id)
```

从商品库删除商品

输入：商品id

返回：状态码

#### updateGoods

``` Java
public int updateGoods(int id, String name, Float purchase_price, Float selling_price, Integer stock, String type)
```

修改商品信息，选项为null时表示不修改

输入：商品id，商品名，商品入货价格，商品出货，商品库存，商品类型

返回：状态码

#### getAllGoods

``` Java
public HashMap<String, Object> getAllGoods()
```

获取商品库中所有的商品

返回：商品键值表，包括(int) returnCode和(Goods[]) result

#### getGoodsById

``` Java
public HashMap<String, Object> getGoodsById(int id)
```

通过id获取商品

输入：商品id

返回：商品键值表，包括(int) returnCode和(Goods) result

#### getGoodsByName

``` Java
public HashMap<String, Object> getGoodsByName(String name)
```

通过名称获取商品

输入：商品名

返回：商品键值表，包括(int) returnCode和(Goods) result

#### getGoodsByType

``` Java
public HashMap<String, Object> getGoodsByType(String type) 
```

通过类型获取商品

输入：商品类型

返回：商品键值表，包括(int) returnCode和(Goods[]) result

#### newPurchaseOrder

``` Java
public int newPurchaseOrder(int goods_id, int count, float total_price, int supplier_id)
```

新建进货订单。进货表增加条目，库存表更改对应商品的库存数量（事务）

输入：商品id，进货数量，进货总价，供应商id

返回：状态码

#### getAllPurchaseOrders

``` Java
public HashMap<String, Object> getAllPurchaseOrders()
```

获取所有进货订单

返回：进货订单键值表，包括(int) returnCode和(PurchaseOrder[]) result

#### newSaleOrder

``` Java
public int newSaleOrder(int goods_id, int count, float total_price, int customer_id)
```

新建出货订单。 出货表增加条目，库存表更改对应商品的库存数量（事务）

输入：商品id，出货数量，出货总价，客户id

返回：状态码

#### getAllSaleOrders

public HashMap<String, Object> getAllSaleOrders()

``` Java
public HashMap<String, Object> getAllSaleOrders()
```

获取所有出货订单

返回：出货订单键值表，包括(int) returnCode和(SaleOrder[]) result

