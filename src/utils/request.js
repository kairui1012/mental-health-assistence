import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
    baseURL: '/api',
    timeout: 5000,
})

service.interceptors.request.use(config => 
    {
        // Do something before request is sent
        const token = localStorage.getItem('token')
        if(token && !config.url?.includes('/login')){
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
        if( data.code === '200' )
        {
            return data.data;
        }
        else
        {
            if(data.code === '-1')
            {
                if(!config.url?.includes('/login'))
                {
                    ElMessage.error(data.msg || '登陆过期,请重新登陆')

                    localStorage.removeItem('token')
                    localStorage.removeItem('userInfo')
                    window.location.href = '/auth/login'
                }
            }
            else
            {
                ElMessage.error(data.msg || '登陆过期,请重新登陆')
                return Promise.reject('网络异常')
            }
        }
        return response;
    }, error => {
        // Do something with response error
        return Promise.reject(error);
    }
);

export default service
