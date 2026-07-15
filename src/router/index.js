import { createRouter, createWebHistory } from 'vue-router'
import BackendLayout from '@/components/BackendLayout.vue'
import nima from '@/components/HelloWorld.vue'

const routes = [
    {
        path: '/back',
        component: BackendLayout,
        children: [
            {
                path: 'dashboard',
                component: ()=> import('@/views/dashboard.vue'),
                meta:{
                    title:'控制台',
                    icon:'PieChart'
                }
            },
             {
                path: 'knowledge',
                component: ()=> import('@/views/dashboard.vue'),
                meta:{
                    title:'知识文章',
                    icon:'ChatLineSquare'
                }
            },
             {
                path: 'consultation',
                component: ()=> import('@/views/consultation.vue'),
                meta:{
                    title:'咨询记录',
                    icon:'Message'
                }
            },
             {
                path: 'emotional',
                component: ()=> import('@/views/emotional.vue'),
                meta:{
                    title:'情绪日志',
                    icon:'User'
                }
            }
        ]
    }
]

console.log(routes[0].children.map(item => item.meta))

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
