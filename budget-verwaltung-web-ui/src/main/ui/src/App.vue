<template>
  <div id="app">
    <h1> {{ budgetVerwaltungAppStateDto.stagingMsg }} </h1>
    <div class="grid-layout">
      <budgetary-item v-for="chartName in budgetVerwaltungAppStateDto.chartNames" :key="chartName"
                      :chart-item-name="chartName"
                      class="chart-container"
      >
      </budgetary-item>
    </div>
    <div v-show="globalErrorMsg">
      <CAlert color="danger" class="error-details tile" style="display: flex">
        {{ globalErrorMsg}}'
      </CAlert>
    </div>
  </div>
</template>

<script>
import BudgetaryItem from "@/components/BudgetaryItem";
import BudgetVerwaltungApi from "@/mixins/BudgetVerwaltungApi";
import '@coreui/coreui/dist/css/coreui.css';

export default {
  name: 'App',
  mixins: [BudgetVerwaltungApi],
  components: {BudgetaryItem},
  computed: {
    budgetVerwaltungAppStateDto: function () {
      return this.$store.getters.budgetVerwaltungAppStateDto;
    },
    applicationTitle: function () {
      return this.$store.getters.applicationTitle;
    },
    globalErrorMsg: function () {
      return this.$store.getters.globalErrorMsg;
    },
  },
  watch: {
    applicationTitle: {
      immediate: true,
      handler() {
        document.title = this.applicationTitle;
      }
    }
  },
  mounted() {
    this.fetchBudgetVerwaltungAppStateDto();
  },
  created() {
    this.fetchApplicationTitle();
  },
}
</script>

<style>
* {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.grid-layout {
  /**
   * Values of the grid (gaps, amount of columns, min-width of the column
    */
  --grid-layout-gap: 10px;
  --grid-column-count: 3;
  --grid-item--min-width: 350px;

  /**
   * Calculated values.
   */
  --gap-count: calc(var(--grid-column-count) - 1);
  --total-gap-width: calc(var(--gap-count) * var(--grid-layout-gap));
  --grid-item--max-width: calc((100% - var(--total-gap-width)) / var(--grid-column-count));

  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(max(var(--grid-item--min-width), var(--grid-item--max-width)), 1fr));
  grid-gap: var(--grid-layout-gap);
  justify-content: center;
  /*  thx to https://css-tricks.com/an-auto-filling-css-grid-with-max-columns/!*/
}

.chart-container {
  max-width: 600px;
  display: grid;
  row-gap: 25px;
}

.tile {
  padding: 10px;
  margin: 10px;
  box-shadow: inset 0 3px 6px rgba(0, 0, 0, 0.16), 0 4px 6px rgba(0, 0, 0, 0.45);
  border-radius: 10px;
}

button {
  border-collapse: collapse;
  background-color: #0095c9;
  border-color: lightskyblue;
  color: white;
}

button:disabled {
  border-collapse: collapse;
  background-color: lightslategray;
  border-color: darkgray;
}

button:disabled {
  background-color: #9F9F9F;
}

/**
Somehow the @coreui/coreui/dist/css/coreui.css styles override the table styles here - and somehow
I couldn't import the coreui-style scoped. So thats why we use !important here
*/

h1, h2, h3, h4, label {
  word-wrap: anywhere;
}

h1, h2, h3, h4 {
  text-align: center;
}

h5 {
  padding-top: 10px;
  word-wrap: anywhere;
}

button {
  white-space: normal;
  word-wrap: break-word;
}

.grid-container {
  display: grid;
  row-gap: 10px;
}

.error-details {
  border-radius: 10px;
}

</style>
