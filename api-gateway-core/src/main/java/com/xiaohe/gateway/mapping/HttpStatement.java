package com.xiaohe.gateway.mapping;


/**
 * 网关接口映射信息
 * web项目中每一个接口都会被封装为 HttpStatement
 * 如 :
 *         HttpStatement httpStatement = new HttpStatement(
 *                 "api-gateway-test",
 *                 "com.xiaohe.gateway.rpc.IActivityBooth",
 *                 "sayHi",
 *                 "/wg/activity/sayHi",
 *                 HttpCommandType.GET
 *         );
 */
public class HttpStatement {
    /**
     * 应用名称
     */
    private String application;

    /**
     * 服务接口全限定类名，如 com.xiaohe.service.UserService
     */
    private String interfaceName;

    /**
     * 服务方法，如 getUser
     */
    private String methodName;

    /**
     * 网关接口, 该接口的路径，比如 HTTP 路径为 /user/getUser
     */
    private String uri;

    /**
     * 接口类型，get、post、delete...
     */
    private HttpCommandType httpCommandType;

    public HttpStatement(String application, String interfaceName, String methodName, String uri, HttpCommandType httpCommandType) {
        this.application = application;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.httpCommandType = httpCommandType;
    }

    public String getApplication() {
        return application;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getUri() {
        return uri;
    }

    public HttpCommandType getHttpCommandType() {
        return httpCommandType;
    }
}
