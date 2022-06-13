/* eslint-disable */
import Vuex from 'vuex';
import axios from 'axios';

const createStore = () => {
  return new Vuex.Store({
    state: () => ({
      items: [],
    }),

    // getters: {
    //   items: state => state.items.map(item => 'task:' + item)
    // },

    mutations: {
      getItems: (state, items) => {
        state.items = items;
      },
    },

    actions: {
      async getItems({ commit }) {
        let { data } = await this.$axios.get('/api/c1/items');
        console.log(data);
        commit('getItems', data);

        // await this.$axios.get('/api/c1/items')
        //   .then(data => {
        //     console.log(data);
        //     commit('getItems', data);
        //   })
        //   .catch(
        //     error => {console.log(error)}
        //   )
      },

      async remove({ commit }, { item }) {
        let { result } = await this.$axios.delete('/api/c1/items/' + item.id);
        let { data } = await this.$axios.get('/api/c1/items');
        commit('getItems', data);
      },

      async changeState({ commit }, { item }) {
        item.status = (item.status == 0) ? 1 : 0;
        let result = await this.$axios.put('/api/c1/items', item);
        let { data } = await this.$axios.get('/api/c1/items');
        commit('getItems', data);
      },

      // async add({ commit }, { name, status }) {
      //   let result = await this.$axios.post('/api/c1/items', { name: name, status: status });
      //   let { data } = await this.$axios.get('/api/c1/items')
      //   commit('getItems', data);
      // },
      async add({ commit }, { deadline,name, status }) {
        let result = await this.$axios.post('/api/c1/items', { deadline: deadline, name: name, status: status });
        let { data } = await this.$axios.get('/api/c1/items')
        commit('getItems', data);
      },

      // 未完成todoを検索
      async search({ commit }, { name,searchDate}) {
        // (方法１:get請求、URLに検索ワードを配置)
        // let { data } = await this.$axios.get('/api/c1/search/' + name);
        // (方法２：post請求、bodyに検索ワードを配置)
        // let { data } = await this.$axios.post('/api/c1/search', { name: name });
        // (方法３：get請求、paramsに検索ワードを配置)
        let { data } = await this.$axios.get('/api/c1/search', { params: { name: name, searchDate: searchDate } });
        // (方法４:get請求、URLに検索ワードを配置、バックエンドでやり方は方法１と異なる)
        // let { data } = await this.$axios.get('/api/c1/search/' + name);
        console.log(data);
        commit('getItems', data);
      }
    }
  })
};
export default createStore
/* eslint-enable */
