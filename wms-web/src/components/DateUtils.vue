<template>
  <div style="padding: 30px;font-size: 30px;">
        <span class="time" id="time">
            今天是：<span class="date">{{ nowTime }}</span>
            <span class="hour" style="margin-left: 5px;">{{ time.hour }}</span>
            <a class="split">:</a>
            <span class="minute">{{ time.minute }}</span>
            <a class="split">:</a>
            <span class="second">{{ time.second }}</span>
        </span>
  </div>
</template>

<script>
export default {
  name: "DateUtils",
  props: ["s"],
  data() {
    return {
      time: {
        hour: "",
        minute: "",
        second: ""
      },
      nowTime: "",
      week: [
        "星期天",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六"
      ]
    };
  },
  mounted() {
    this.dateTime();
  },
  methods: {
    dateTime() {
      this.timeFormat();
      setTimeout(() => {
        this.dateTime();
      }, 1000);
    },
    timeFormat() {
      const newTime = new Date();
      this.time.hour = this.getIncrease(newTime.getHours(), 2);
      this.time.minute = this.getIncrease(newTime.getMinutes(), 2);
      this.time.second = this.getIncrease(newTime.getSeconds(), 2);
      this.nowTime =
          this.getIncrease(newTime.getFullYear(), 4) +
          "年" +
          this.getIncrease(newTime.getMonth() + 1, 2) +
          "月" +
          this.getIncrease(newTime.getDate(), 2) +
          "日 " +
          this.week[newTime.getDay()];
      //this.hour=
      // return `${this.time.hour}:${this.time.minute}:${this.time.second}`;
    },
    getIncrease(num, digit) {
      let increase = "";
      for (let i = 0; i < digit; i++) {
        increase += "0";
      }
      return (increase + num).slice(-digit);
    }
  }
}
</script>

<style scoped>
.txt-data .time {
  font-size: 1rem;
  margin-right: 0.5rem;
}

.split {
  animation: shark 1s step-end infinite;
  vertical-align: center;
  margin-left: 2px;
  margin-right: 2px;
}

@keyframes shark {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}
</style>