import VueRouter from 'vue-router'
import Login from '@/components/Login.vue'
import Index from '@/components/Index.vue'
import Home from '@/components/Home.vue'

// 解决跳转到当前路由报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch((err) => err)
}

const routes = [
    {
        path: '/',
        name: 'login',
        component: Login
    },
    {
        path: '/Index',
        name: 'index',
        component: Index,
        children: [
            {
                path: '/Home',
                name: 'home',
                meta: {
                    title: '首页'
                },
                component: Home
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router
