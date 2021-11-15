**报错备忘**:
- 01 There was an unexpected error (type=Internal Server Error, status=500).Circular view path [index]: would dispatch back to the current handler URL:

![image](https://user-images.githubusercontent.com/55024146/141726007-667be23a-a506-4ef5-91cb-c20eede21173.png)

解决姿势
> 在请求方法上加注解：@ResponseBody，无需模板文件

![image](https://user-images.githubusercontent.com/55024146/141726316-efae5b21-9b5f-41f9-9797-9c2ab6e61bdf.png)

