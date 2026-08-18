import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
    baseURL: '/api',
    timeout: 30000,
})

service.interceptors.request.use(config => 
    {
        // Do something before request is sent
        const token = localStorage.getItem('token')
        const isPublicRequest = config.url?.includes('/login') || config.url?.includes('/user/add')

        if(token && !isPublicRequest){
            config.headers['token'] = token
        }
        return config;
    }, error => {
        // Do something with request error
        return Promise.reject(error);
    }
);

service.interceptors.response.use(response => 
    {
        // Do something before response is sent
        const {data,config} = response
        if( String(data.code) === '200' )
        {
            return data.data;
        }
        else
        {
            const isPublicRequest = config.url?.includes('/login') || config.url?.includes('/user/add')

            // 原逻辑：所有 code 为 -1 的响应都会被视为登录过期并跳转登录页。
            const isLoginExpired = String(data.code) === '-1' &&
                /登录过期|登陆过期|token.*过期|token.*失效/i.test(data.msg || '')

            if (isLoginExpired && !isPublicRequest)
            {
                ElMessage.error(data.msg || '登录过期，请重新登录')

                localStorage.removeItem('token')
                localStorage.removeItem('userInfo')
                window.location.href = '/auth/login'
                return Promise.reject(new Error(data.msg || '登录已过期'))
            }

            // 对话服务繁忙等普通业务错误只提示，不清除登录状态。
            ElMessage.error(data.msg || '请求失败，请稍后重试')
            return Promise.reject(new Error(data.msg || '请求失败'))
        }
        return response;
    }, error => {
        // Do something with response error
        return Promise.reject(error);
    }
);

export default service
