# 代码审核规则集

<!-- 
  填写指南：
  1. 每个规则以 "## 规则名称" 开头，下方包含固定字段
  2. 所有字段为必填项，代码示例至少各提供1个
  3. 同一类型的多个示例（如多个正例）使用 `---` 分隔
  4. 填写完成后可删除本注释块（不影响解析）

  字段说明：
  - 规则名称：简洁描述规则核心（如"循环中对象复用"）
  - 编程语言：填写单种语言（如Go、Java、Python，仅支持一种）
  - 问题描述：说明问题表现和影响（如"未关闭文件句柄导致资源泄漏"）
  - 问题分类：从以下选项选择：
    代码格式、边界条件、类型安全、资源泄漏、错误处理、
    性能问题、安全问题、硬编码问题、可读性问题、代码设计问题、
    代码逻辑问题、拼写错误、测试规范
  - 问题等级：从以下选项选择：
    Critical issues（必须修复，影响功能/安全）、
    Needs to improve（需要优化，不影响核心功能）、
    Nice to have（建议优化，仅提升体验）
  - 代码正例：正确写法，多个示例使用 `---` 分隔
  - 代码反例：错误写法，多个示例使用 `---` 分隔
-->


## 资源释放
- **编程语言**：Go
- **问题描述**：未正确关闭资源（如文件、网络连接、数据库连接等）会导致资源泄漏，长时间运行的程序可能会耗尽系统资源
- **问题分类**：资源泄漏
- **问题等级**：Critical issues（必须修复，影响功能/安全）
- **代码正例**：
```
file, err := os.Open("file.txt")
if err != nil {
    return err
}
defer file.Close()
// 处理文件内容
```
    ---
```
conn, err := net.Dial("tcp", "example.com:80")
if err != nil {
    return err
}
defer conn.Close()
// 使用连接
```
- **代码反例**：
```
file, err := os.Open("file.txt")
if err != nil {
    return err
}
// 没有关闭文件，可能导致资源泄漏
// 处理文件内容
```
    ---
```
func processData() error {
    db, err := sql.Open("mysql", "user:password@/dbname")
    if err != nil {
        return err
    }
    // 缺少 defer db.Close()
    rows, err := db.Query("SELECT * FROM table")
    // 处理查询结果
    return nil
}
```

## 错误处理
- **编程语言**：Go
- **问题描述**：忽略错误返回值会导致程序在出现问题时继续执行，可能引发更严重的错误或数据损坏
- **问题分类**：错误处理
- **问题等级**：Critical issues（必须修复，影响功能/安全）
- **代码正例**：
```
data, err := ioutil.ReadFile("config.json")
if err != nil {
    log.Fatalf("无法读取配置文件: %v", err)
}
var config Config
if err := json.Unmarshal(data, &config); err != nil {
    log.Fatalf("解析配置失败: %v", err)
}
```
    ---
```
result, err := db.Exec("UPDATE users SET status = ? WHERE id = ?", "active", userID)
if err != nil {
    return fmt.Errorf("更新用户状态失败: %w", err)
}
affected, err := result.RowsAffected()
if err != nil {
    return fmt.Errorf("获取影响行数失败: %w", err)
}
if affected == 0 {
    return fmt.Errorf("未找到ID为%d的用户", userID)
}
```
- **代码反例**：
```
data, _ := ioutil.ReadFile("config.json") // 忽略错误
var config Config
json.Unmarshal(data, &config) // 忽略错误
```
    ---
```
db.Exec("UPDATE users SET status = ? WHERE id = ?", "active", userID)
// 没有检查错误或影响的行数
```

## 并发安全
- **编程语言**：Go
- **问题描述**：在并发环境中未使用适当的同步机制访问共享资源，可能导致数据竞争和不确定的行为
- **问题分类**：安全问题
- **问题等级**：Critical issues（必须修复，影响功能/安全）
- **代码正例**：
```
type Counter struct {
    mu    sync.Mutex
    count int
}

func (c *Counter) Increment() {
    c.mu.Lock()
    defer c.mu.Unlock()
    c.count++
}

func (c *Counter) Value() int {
    c.mu.Lock()
    defer c.mu.Unlock()
    return c.count
}
```
    ---
```
var (
    cache     = make(map[string]string)
    cacheLock = sync.RWMutex{}
)

func Get(key string) (string, bool) {
    cacheLock.RLock()
    defer cacheLock.RUnlock()
    val, ok := cache[key]
    return val, ok
}

func Set(key, value string) {
    cacheLock.Lock()
    defer cacheLock.Unlock()
    cache[key] = value
}
```
- **代码反例**：
```
type Counter struct {
    count int
}

func (c *Counter) Increment() {
    c.count++ // 在并发环境中不安全
}

func (c *Counter) Value() int {
    return c.count // 在并发环境中可能返回不一致的值
}
```
    ---
```
var cache = make(map[string]string)

func Get(key string) (string, bool) {
    val, ok := cache[key] // 并发读取不安全
    return val, ok
}

func Set(key, value string) {
    cache[key] = value // 并发写入不安全
}
```

## SQL注入防护
- **编程语言**：Java
- **问题描述**：直接拼接SQL语句容易导致SQL注入攻击，可能使攻击者执行未授权的数据库操作
- **问题分类**：安全问题
- **问题等级**：Critical issues（必须修复，影响功能/安全）
- **代码正例**：
```
String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, username);
stmt.setString(2, password);
ResultSet rs = stmt.executeQuery();
```
    ---
```
public User findByEmail(String email) {
    String sql = "SELECT id, name, email FROM users WHERE email = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email")
                );
            }
            return null;
        }
    }
}
```
- **代码反例**：
```
String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```
    ---
```
public boolean authenticateUser(String username, String password) {
    // 危险：直接拼接用户输入到SQL语句中
    String sql = "SELECT count(*) FROM users WHERE username = '" + username + 
                 "' AND password = '" + password + "'";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    return rs.next() && rs.getInt(1) > 0;
}
```

## 空指针检查
- **编程语言**：Java
- **问题描述**：在访问对象或调用方法前未检查空引用，可能导致NullPointerException异常
- **问题分类**：边界条件
- **问题等级**：Critical issues（必须修复，影响功能/安全）
- **代码正例**：
```
public String getUserName(User user) {
    if (user == null) {
        return "Guest";
    }
    return user.getName();
}
```
    ---
```
public void processOrder(Order order) {
    if (order == null) {
        throw new IllegalArgumentException("Order cannot be null");
    }
    
    List<Item> items = order.getItems();
    if (items != null && !items.isEmpty()) {
        for (Item item : items) {
            // 处理每个商品
        }
    }
}
```
- **代码反例**：
```
public String getUserName(User user) {
    return user.getName(); // 如果user为null，将抛出NullPointerException
}
```
    ---
```
public void processOrder(Order order) {
    List<Item> items = order.getItems(); // order可能为null
    for (Item item : items) { // items可能为null
        // 处理每个商品
    }
}
```

## 内存泄漏
- **编程语言**：JavaScript
- **问题描述**：在闭包中引用大型对象或DOM元素但未正确清理，导致内存无法被垃圾回收
- **问题分类**：资源泄漏
- **问题等级**：Needs to improve（需要优化，不影响核心功能）
- **代码正例**：
```
function setupEventHandlers() {
  const button = document.getElementById('myButton');
  
  // 使用弱引用存储DOM元素
  let cleanup = () => {
    button.removeEventListener('click', handleClick);
    button = null; // 允许垃圾回收
  };
  
  function handleClick() {
    // 处理点击事件
  }
  
  button.addEventListener('click', handleClick);
  
  return cleanup; // 返回清理函数
}

// 使用
const cleanup = setupEventHandlers();
// 当不再需要时
cleanup();
```
    ---
```
class ResourceManager {
  constructor() {
    this.resources = new Map();

  }
  
  addResource(id, resource) {
    this.resources.set(id, resource);
  }
  
  getResource(id) {
    return this.resources.get(id);
  }
  
  releaseResource(id) {
    const resource = this.resources.get(id);
    if (resource && typeof resource.dispose === 'function') {
      resource.dispose();
    }
    this.resources.delete(id);
  }
  
  releaseAll() {
    for (const [id, resource] of this.resources.entries()) {
      if (typeof resource.dispose === 'function') {
        resource.dispose();
      }
    }
    this.resources.clear();
  }
}
```
- **代码反例**：
```
function createLargeDataProcessor() {
  // 大型数据对象
  const lar
geData = loadLargeDataSet();
  
  // 返回的函数持有对largeData的引用
  return function process() {
    // 即使不再需要largeData，它也不会被垃圾回收
    return largeData.filter(item => item.value > 100);
  };
}

// 创建处理器
const processor = createLargeDataProcessor();
// 使用处理器
processor();
// 没有方法释放largeData
```
    ---
```
function setupObserver() {
  const element = document.getElementById('observed');
  const observer = new MutationObserver(() => {
    console.log('Element changed:', element.innerHTML);
  });
  
  observer.observe(element, { childList: true, subtree: true });
  
  // 没有提供方法来断开观察器，导致内存泄漏
}
```

## 异步错误处理
- **编程语言**：JavaScript
- **问题描述**：未正确处理Promise中的错误，导致未捕获的异常和静默失败
- **问题分类**：错误处理
- **问题等级**：Critical issues（必须修复，影响功能/安全）
- **代码正例**：
```
async function fetchUserData(userId) {
  try {
    const response = await fetch(`/api/users/${userId}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Failed to fetch user data:', error);
    throw error; // 重新抛出以便调用者处理
  }
}

// 使用
fetchUserData(123)
  .then(user => {
    // 处理用户数据
  })
  .catch(error =>
