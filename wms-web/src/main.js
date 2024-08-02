import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css';
import axios from 'axios';
import VueRouter from 'vue-router';
import router from './router/index';
import store from './store';

Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl='http://localhost:8090'

Vue.use(ElementUI);
Vue.use(VueRouter)
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')
