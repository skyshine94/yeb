# yeb业务梳理

一、逆向工程模块

1.1 准备工作：
1. 构建yeb-generator模块。
  2)	引入mysql、mybatis-plus、mybatis-plus代码生成器、freemarker模板依赖。
  3)	引入CodeGenerator代码生成类，设置参数。
二、登录模块
1.	准备工作：
  1)	构建yeb-server模块，创建配置类配置分页插件并加入@MapperScan扫描mapper。
  2)	引入lombok、mysql、mybatis-plus、swagger2、security、jwt、google验证码依赖。
  3)	引入验证码配置类。
  4)	创建swagger配置类。
  5)	在配置文件中配置tokenHeader、secret、expiration、tokenHead。
  6)	创建全局异常处理类处理sql异常。
  7)	编写jwt工具类JwtTokenUtil，根据用户信息生成token，根据token获取用户信息，验证token是否有效。
  8)	创建RespBean公共返回类。
  9)	在Admin类中添加角色列表字段，实现UserDetails接口，在enabled字段上加入@Getter(AccessLevel.NONE)禁用get方法，使用UserDetails接口实现的get，使用stream流调用SimpleGrantedAuthority对象实现获取权限集合方法。
  10)	在Menu类中添加角色列表字段。
  11)	编写jwt登录授权过滤器继承OncePerRequestFilter：
    调用HttpServletRequest获取请求头数据。
    判断token是否存在。
    从token中获取登录用户名。
    判断是否用户名存在且未存入安全上下文中。
    根据用户名登录。
    判断token是否失效。
    将用户信息存入request中。
    将用户信息存入安全上下文。
  12)	编写处理认证用户访问无权限资源时的异常类实现AccessDeniedHandler。
  13)	编写处理匿名用户访问无权限资源时的异常类实现AuthenticationEntryPoint。
  14)	编写用户权限设置类实现FilterInvocationSecurityMetadataSource：
    获取请求url。
    获取角色和对应的菜单的List集合。
    遍历List集合，判断请求url与菜单url是否匹配。
    获取请求url所需角色List集合。
    匹配失败的url默认为登录角色。
  15)	编写决策管理类实现AccessDecisionManager：
    获取url所需角色集合。
    遍历集合，判断角色是否为登录角色，是则放行。
    获取用户角色集合。
    判断请求url所需角色是否包含在用户角色中，是则放行。
  16)	编写security配置类继承WebSecurityConfigurerAdapter：
    配置放行路径。
    配置自定义UserDetailsService和密码解析器。
    禁用crsf和session。
    配置需要认证路径。
    配置动态权限。
    禁用缓存。
    添加jwt登录授权过滤器。
    添加处理认证和匿名用户访问无权限资源时的异常类。
2.	业务：
  1)	获取登录验证码：（CaptchaController）
    在方法上加入@GetMapping(value = "/captcha", produces = "image/jpeg")防止图片乱码。
    设置输出图片的响应头信息。
    调用DefaultKaptcha获取验证码文本。
    将验证码放到session中。
    调用DefaultKaptcha根据文本创建图形验证码。
    调用ImageIO.write方法。
  2)	登录之后返回token：（LoginController）
    从session中获取验证码。
    判断验证码是否匹配。
    调用UserDetailsService根据用户名获取用户信息。
    判断用户信息是否为空，密码是否匹配。
    判断用户是否被禁用。
    创建UsernamePasswordAuthenticationToken对象，并存入安全上下文中。
    调用jwt工具类根据用户信息生成token。
    创建Map集合封装tokenHead和token。
  3)	获取登录用户信息：（LoginController）
    调用Principal从安全上下文中接收当前登录对象。
    调用Principal的getName方法获取用户名。
    调用mp方法，根据用户名和是否启用获取用户信息。
    调用mapper方法。
    role左关联admin_role，根据adminId获取role列表。
  4)	退出登录：（LoginController）
    返回注销成功，由前端删除token令牌。
三、菜单模块
1.	准备工作：
  1)	引入redis、common-pool2线程池依赖。
  2)	创建redis配置类，配置序列化。
  3)	编写获取当前用户信息工具类，从安全上下文中获取当前用户信息。
2.	业务：
  1)	根据用户id查询菜单列表：（MenuController）
    调用获取当前用户信息工具类，获取用户id。
    从redis中获取菜单信息。
    判断菜单信息是否为空。
    为空则调用mapper方法。
    menu自关联、关联admin_role、关联menu_role，根据id查询admin_role的adminId。
    将菜单信息放入redis中。
四、职位管理模块
1.	准备工作：
  1)	在所有类的日期字段上加入@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")格式化日期。
2.	业务：
  1)	获取所有职位：（PositionController）
    调用mp方法。
  2)	添加职位：（PositionController）
    调用mp方法。
  3)	更新职位：（PositionController）
    调用mp方法。
  4)	删除职位：（PositionController）
    调用mp方法。
  5)	批量删除职位：（PositionController）
    调用mp方法。
五、职称管理模块
1.	业务：
  1)	获取所有职称：（JoblevelController）
    调用mp方法。
  2)	添加职称：（JoblevelController）
    调用mp方法。
  3)	更新职称：（JoblevelController）
    调用mp方法。
  4)	删除职称：（JoblevelController）
    调用mp方法。
  5)	批量删除职称：（JoblevelController）
    调用mp方法。
六、权限组管理模块
1.	业务：
  1)	获取所有角色：（PermissionController）
    调用mp方法。
  2)	添加角色：（PermissionController）
    判断角色名是否以ROLE_开头。
    否则在角色名前添加ROLE_前缀。
    调用mp方法。
  3)	删除角色：（PermissionController）
    调用mp方法。
  4)	获取所有菜单：（PermissionController）
    调用mapper方法。
    menu自关联两次。
  5)	根据角色id查询菜单id：（PermissionController）
    调用mp方法，根据rid获取菜单列表。
    使用stream流获取mid并转换成List集合。
  6)	根据角色id和菜单id更新角色菜单：（PermissionController）
    调用mp方法，根据rid删除角色菜单。
    调用mapper方法。
    遍历mid数组，在menu_role中根据rid和mid插入数据。
七、部门管理模块
1.	准备工作：
  1)	在Department实体类中添加子部门列表children字段和result字段（接收存储过程结果）。
2.	业务：
  1)	获取所有部门：（DepartmentController）
    调用mapper方法。
    根据parentId = -1查询部门列表，并将结果的id作为parentId递归查询。
  2)	添加部门：（DepartmentController）
    调用mapper方法。
    在<insert>标签中设置statementType="CALLABLE"。
    调用存储过程。
    添加部门。
    查询父部门的depPath。
    在父部门路径后拼接子部门路径，更新部门。
    将父部门的isParent更新为true。
  3)	删除部门：（DepartmentController）
    调用mapper方法。
    在<delete>标签中设置statementType="CALLABLE"。
    调用存储过程。
    根据部门id和isParent查询部门总数。
    如果部门总数为0，表示该部门下有子部门，将result设置为-2。
    根据部门id查询员工总数。
    如果员工总数大于0，表示该部门下有员工，将result设置为-1。
    根据部门id查询父部门id。
    根据部门id删除部门。
    根据父部门id查询父部门下子部门总数。
    如果父部门下子部门总数为0，表示该父部门下没有子部门，将isParent设置为false。
八、操作员管理模块
1.	业务：
  1)	带条件获取操作员列表：（AdminController）
    调用获取当前用户信息工具类，获取用户id。
    调用mapper方法。
    admin左关联admin_role、左关联role，根据id和name模糊查询操作员列表。
  2)	删除操作员：（AdminController）
    调用mp方法。
  3)	更新操作员：（AdminController）
    调用mp方法。
  4)	获取所有角色：（AdminController）
    调用mp方法。
  5)	更新操作员角色：（AdminController）
    调用mp方法，根据adminId删除操作员角色。
    调用mapper方法。
    遍历rid数组，在admin_role中根据adminId和rid插入数据。
九、员工管理模块
1.	准备工作：
  1)	创建RespPageBean分页公共返回类。
  2)	编写全局日期格式转换类，用于转换前端传入的日期参数。
  3)	在Employee类中添加民族、政治面貌、部门、职称和职位字段。
  4)	引入easy poi依赖。
  5)	在Employee类的属性上添加@Excel，对于引用类型属性添加@ExcelEntity。
  6)	由于民族、政治面貌、部门、职称、职位的name属性唯一，需要重写equals和hashCode方法，在类上加入@EqualsAndHashCode(callSuper = false, of = "name")、加入@RequiredArgsConstructor添加有参构造，在name字段上加入@NonNull。
2.	业务：
  1)	分页带条件获取员工信息：（EmployeeController）
    创建Page对象封装分页参数。
    调用mapper方法。
    employee关联nation、关联politics_status、关联department、关联joblevel、关联position，根据条件参数获取员工信息。
  2)	获取所有民族：（EmployeeController）
    调用mp方法。
  3)	获取所有政治面貌：（EmployeeController）
    调用mp方法。
  4)	获取所有部门：（EmployeeController）
    调用mp方法。
  5)	获取所有职称：（EmployeeController）
    调用mp方法。
  6)	获取所有职位：（EmployeeController）
    调用mp方法。
  7)	获取工号：（EmployeeController）
    调用mp方法，获取workID的最大值。
    将workID的值加一。
  8)	添加员工：（EmployeeController）
    获取合用起始和终止日期，处理合同期限。
    创建DecimalFormat对象格式化合同期限。
    调用mp方法。
  9)	更新员工：（EmployeeController）
    调用mp方法。
  10)	删除员工：（EmployeeController）
    调用mp方法。
  11)	导出员工信息：（EmployeeController）
    调用mapper方法。
    employee关联nation、关联politics_status、关联department、关联joblevel、关联position，获取所有员工信息。
    设置导出表格的响应头信息。
    使用HttpServletResponse获取输出流。
    调用ExcelExportUtil.exportExcel方法获取Workbook对象。
    调用Workbook.write方法。
  12)	导入员工信息：（EmployeeController）
    创建ImportParams对象，调用setTitleRows(1)去除标题行。
    调用MultipartFile获取输入流。
    调用ExcelImportUtil.importExcel方法获取员工信息列表。
    使用stream流封装民族、政治面貌、部门、职称、职位的id。
    调用mp方法批量添加员工信息。
十、工资账套模块
1.	业务：
  1)	获取所有工资账套：（SalaryController）
    调用mp方法。
  2)	添加工资账套：（SalaryController）
    封装启用时间。
    调用mp方法。
  3)	更新工资账套：（SalaryController）
    调用mp方法。
  4)	删除工资账套：（SalaryController）
    调用mp方法。
十一、员工账套模块
1.	准备工作：
  1)	在Employee类中添加员工账套字段。
2.	业务：
  1)	获取所有工资账套：（SalarySobCfgController）
    调用mp方法。
  2)	分页获取所有员工账套：（SalarySobCfgController）
    创建Page对象封装分页参数。
    调用mapper方法。
    employee左关联salary、左关联department。
  3)	更新员工账套：（SalarySobCfgController）
    调用mp方法，根据id更新员工账套。
十二、个人中心模块
1.	准备工作：
  1)	创建角色集合反序列化类AdminAuthorityDeserializer继承JsonDeserializer，手动反序列化，避免更新用户角色信息时出错。
  2)	在Admin类的getAuthorities方法上加入@JsonDeserialize(using = AdminAuthorityDeserializer.class)。
2.	业务：
  1)	更新用户信息：（AdminInfoController）
    调用mp方法。
    将更新后的用户信息存入安全上下文。
  2)	更新用户密码：（AdminInfoController）
    调用mp方法，根据adminId获取用户信息。
    判断密码是否匹配。
    调用mp方法。
十三、邮件模块
1.	准备工作：
  1)	构建yeb-mail模块，在启动入口类上加入@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)，关闭数据库自动配置。
  2)	引入mail、thymeleaf、yeb-server依赖。
  3)	在配置文件中开启rabbitmq消息确认回调和消息失败回调。（yeb-server）
  4)	在配置文件中开启rabbitmq手动确认。
  5)	引入rabbitmq依赖。（yeb-server）
  6)	编写邮件模板mail.html。
  7)	编写常量类MailConstants。
  8)	编写rabbitmq配置类：（yeb-server）
    调用RabbitTemplate.setConfirmCallback方法配置消息确认回调，调用mp方法更新消息状态。
    调用RabbitTemplate.setReturnsCallback方法配置消息失败回调。
  9)	创建redis配置类，配置序列化。
2.	业务：
  1)	修改添加员工：（yeb-server）
    封装MailLog。
    调用mp方法添加消息记录。
    调用mapper方法。
    employee关联nation、关联politics_status、关联department、关联joblevel、关联position，根据id获取员工信息。
    调用RabbitTemplate发送消息，创建CorrelationData对象传递消息id。
  2)	每隔10秒执行邮件发送：（yeb-server）
    创建定时任务类，加入@EnableScheduling，在方法上加入@Scheduled(cron = "0/10 * * * * ?")。
    调用mp方法根据消息状态和重试时间获取消息列表。
    调用forEach遍历集合。
    判断是否超过重试次数。
    超过则调用mp方法根据msgId更新消息状态。
    不超过则调用mp方法根据msgId更新重试次数、更新时间、重试时间。
    调用mp方法根据eid获取员工信息。
    调用RabbitTemplate发送消息，创建CorrelationData对象传递消息id。
  3)	创建消息监听器：
    调用Message接收数据，获取员工信息、消息序号、msgId。
    幂等性校验，从redis中获取msgId。
    判断是否接收到重复消息。
    是则调用channel.basicAck手动确认消息。
    否则调用JavaMailSender创建MimeMessage对象。
    创建MimeMessageHelper对象，传入MimeMessage。
    调用MimeMessageHelper设置邮件参数。
    创建Context对象，设置邮件内容。
    调用TemplateEngine引入mail.html邮件模板。
    调用JavaMailSender发送邮件，传入MimeMessage。
    将msgId放入redis中。
    调用channel.basicAck手动确认消息。
    发生异常时调用channel.basicNack(tag, false, true)退回到队列。
