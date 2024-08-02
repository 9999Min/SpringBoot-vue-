import vue from 'vue'
import Vuex from 'vuex'
import router from "../router"

vue.use(Vuex)

function addNewRoute(menuList) {
    if (!menuList) return
    menuList.forEach(menu => {
        let childRoute = {
            path: '/' + menu.menuClick,
            name: menu.menuName,
            meta: {
                title: menu.menuName
            },
            component: () => import('../components/' + menu.menuComponent)
        }
        router.addRoute('index',childRoute)
    })
}

export default new Vuex.Store({
    state: {
        menu: []
    },
    mutations: {
        setMenu(state, menuList) {
            state.menu = menuList
            addNewRoute(menuList)
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
})