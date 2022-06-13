<template>
  <div>
    <b-container>
      <h1 class="text-dark text-center mb-4">
        Todo List
      </h1>
      <!--========================== メッセージ出力アリア ==========================-->
      <b-alert
        :show="dismissCountDown"
        dismissible
        variant="danger"
        @dismissed="dismissCountDown = 0"
        @dismiss-count-down="countDownChanged"
      >
        {{ msg }} {{ dismissCountDown }} 秒...
      </b-alert>

      <!--========================== ToDo内容を追加 ==========================-->
      <b-card class="bg-light mb-3">
        <b-row class="text-left">
          <b-col md="2" class="mt-1">
            <label>追加ToDo内容
              <span
                class="
                  badge
                  align-middle
                  badge-pill badge-danger badge-outlined
                "
              >必須</span></label>
          </b-col>
          <b-col md="5" class="text-left">
            <input
              v-model="inputtext"
              type="text"
              class="form-control"
              placeholder="ToDo内容を入力"
              pattern="^[a-zA-Z0-9]+$"
            >
          </b-col>
        </b-row>
        <b-row class="text-left">
          <b-col md="2">
            <label>ToDo完成期限
              <span class="badge badge-danger badge-outlined">必須</span></label>
          </b-col>
          <b-col md="5">
            <b-form-datepicker
              id="example-datepicker"
              v-model="deadline"
              :min="min"
              label-no-date-selected="日付を選択"
              label-help=""
              date-format-options="{ year: 'numeric', month: 'short', day: '2-digit' }"
            />
          </b-col>
          <b-col md="2" offset="1">
            <b-button variant="outline-info" @click="add">
              <b-icon icon="plus" /> 追加
            </b-button>
          </b-col>
          <b-col md="2">
            <b-button variant="outline-info" @click="clear">
              クリア
            </b-button>
          </b-col>
        </b-row>
      </b-card>
      <!--========================== 未完成todo検索 ==========================-->
      <b-card class="bg-light mb-3">
        <b-row class="text-left">
          <b-col md="2" class="mt-1">
            <label>未完成ToDo検索</label>
          </b-col>
          <b-col md="5">
            <input
              v-model="inputWord"
              type="text"
              class="form-control"
              placeholder="キーワードを入力"
              pattern="^[a-zA-Z0-9]+$"
            >
          </b-col>
          <b-col md="2" offset="1">
            <b-button variant="outline-info" @click="search">
              <b-icon icon="search" /> 検索
            </b-button>
          </b-col>
        </b-row>
        <!-- 指定期限日までのToDoを検索 -->
        <b-row class="text-left">
          <b-col md="2">
            <label>指定期限日まで未完成ToDo検索</label>
          </b-col>
          <b-col md="5">
            <v-app class="vuetify-app">
              <v-menu
                v-model="menu"
                :close-on-content-click="false"
                :nudge-right="40"
                :nudge-bottom="80"
                transition="scale-transition"
                min-width="auto"
              >
                <!-- <template #activator="{ on, attrs }"> -->
                <template #activator="{ on, attrs }">
                  <v-text-field
                    v-model="searchDate"
                    label="日付を選択"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                  />
                </template>
                <v-date-picker
                  v-model="searchDate"
                  color="green lighten-1"
                  :allowed-dates="allowedDates"
                  @input="menu = false"
                />
              </v-menu>
            </v-app>
          </b-col>
        </b-row>
      </b-card>
      <!--========================== 件数と一覧出力アリア ==========================-->
      <b-card>
        <b-row class="text-right">
          <b-col md="12">
            <span class="bg-warning text-warning">■</span>
            <span> 未完成件数：{{ total - finish }} </span>
            <span class="bg-success text-success ml-4">■</span>
            <span> 完成件数：{{ finish }} </span>
          </b-col>
        </b-row>
        <table class="table">
          <thead>
            <tr>
              <th scope="col">
                ID
              </th>
              <th scope="col">
                ToDo内容
              </th>
              <th scope="col">
                完成期限
              </th>
              <th scope="col">
                状態
              </th>
              <th scope="col">
                削除
              </th>
            </tr>
          </thead>
          <tbody>
            <!-- <p v-if="dataNum == 0">
              {{ noData }}
            </p> -->
            <tr v-for="item in items" :key="item.id">
              <td scope="row">
                {{ item.id }}
              </td>
              <td>{{ item.name }}</td>
              <td>{{ item.deadline }}</td>
              <td>
                <b-button
                  type="button"
                  variant="outline-primary"
                  @click="$store.dispatch('changeState', { item: item })"
                >
                  <span v-if="item.status == 0" class="text-warning">
                    todo
                  </span>
                  <span v-else class="text-success"> done </span>
                </b-button>
              </td>
              <td>
                <b-button
                  type="button"
                  variant="outline-primary"
                  @click="$store.dispatch('remove', { item: item })"
                >
                  <b-icon icon="trash" />
                </b-button>
              </td>
            </tr>
          </tbody>
        </table>
      </b-card>
    </b-container>
  </div>
</template>

<script>
/* eslint-disable */
import { mapState } from "vuex";

export default {
  data() {
    const now = new Date();
    // const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    // const minDate = new Date(today);
    const minDate = now.toISOString().substr(0, 10);

    return {
      // vuetify date picker使用
      searchDate: new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
        .toISOString()
        .substr(0, 10),
      menu: false,

      // noData: "データなし",
      dataNum: 0,

      msg: null,
      deadline: "",
      inputtext: "",
      inputWord: "",
      dismissSecs: 5,
      dismissCountDown: 0,
      min: now,
    };
  },

  computed: {
    ...mapState(["items"]),
    finish() {
      let sum = 0;
      for (const item of this.items) {
        sum += Number(item.status);
      }
      return sum;
    },
    total() {
      // return this.$store.state.items.length;
      return this.items.length;
    },
  },

  created() {
    this.$store.dispatch("getItems");
    this.dataNum = this.items.length;
  },

  mounted() {},

  methods: {
    allowedDates: (val) => Date.parse(val) > Date.now() - 8.64e7,
    add() {
      if (
        this.inputtext !== "" &&
        this.inputtext.length <= 64 &&
        this.deadline !== ""
      ) {
        this.$store.dispatch("add", {
          deadline: this.deadline,
          name: this.inputtext,
          status: 0,
        });
        // this.inputtext = "";
      } else if (this.inputtext.length > 64) {
        this.msg = "ToDo内容が長すぎます。64文字以内で入力してください。";
        this.dismissCountDown = this.dismissSecs;
      } else if (this.inputtext == "") {
        this.msg = "ToDo内容を入力してください。";
        this.dismissCountDown = this.dismissSecs;
      } else {
        this.msg = "ToDo完成期限を入力してください。";
        this.dismissCountDown = this.dismissSecs;
      }
    },
    clear() {
      this.deadline = "";
      this.inputtext = "";
    },
    countDownChanged(dismissCountDown) {
      this.dismissCountDown = dismissCountDown;
    },
    // 未完成todoを検索
    search() {
      if (this.inputWord !== "" || this.searchDate !== "") {
        this.$store.dispatch("search", {
          name: this.inputWord,
          searchDate: this.searchDate,
        });
        // this.inputWord = "";
      } else {
        this.msg = "検索キーワードまたは期限を入力してください。";
        this.dismissCountDown = this.dismissSecs;
      }
    },
  },
};
</script>
<style lang="scss">
.vuetify-app {
  max-height: 4em;
}
// @import "../node_modules/bootstrap/scss/functions";
// @import "../node_modules/bootstrap/scss/variables";
// @import "../node_modules/bootstrap/scss/maps";
// @import "../node_modules/bootstrap/scss/mixins";
// @import "../node_modules/bootstrap/scss/root";

// .table-header {
//   color: $yellow-300;
//   background-color: $indigo-900;
// }
</style>