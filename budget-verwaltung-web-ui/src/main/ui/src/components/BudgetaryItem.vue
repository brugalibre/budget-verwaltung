<template>
  <div class="tile budgetary-item"
       :key="budgetaryItemRefreshKey"
       ref="budgetaryContainer">
    <h2>{{ chartItemName }}</h2>
    <budgetary-item-add-value
        :chart-item-name="chartItemName"
        :column-dtos="chartDto.columnDtos"
        @refreshBudgetaryItem="refreshBudgetaryItem"
    />
    <column-chart
        title="Monats Übersicht"
        style="overflow-x: auto"
        :data="'/api/v1/budget-verwaltung/getChartData/' + chartItemName"
        :dataset="{borderColor: '#004587', backgroundColor: '#358fe6'}"
        empty="Keine Daten"
        loading="Lade Daten..."
        suffix="Chf"
        :ytitle="chartDto.yAxisLabel"
        :xtitle="chartDto.xAxisLabel"
    >
    </column-chart>
  </div>
</template>

<script>
import BudgetVerwaltungApi from "@/mixins/BudgetVerwaltungApi";
import BudgetaryItemAddValue from "@/components/BudgetaryItemAddValue";
import 'vue-loading-overlay/dist/vue-loading.css';
import {BUDGET_VERWALTUNG_REST_API_URL} from "@/mixins/CommonBudgetVerwaltungRestApi";
import * as logger from '@/mixins/BudgetVerwaltungLogger';

export default {
  name: 'BudgetaryItem',
  mixins: [BudgetVerwaltungApi],
  components: {
    BudgetaryItemAddValue
  },
  props: ['chartItemName'],
  data() {
    return {
      chartDto: {
        columnDtos: [],
        xAxisLabel: '',
        yAxisLabel: '',
      },
      budgetaryItemRefreshKey: 0,
    }
  },
  methods: {
    fetchChartDto: function () {
      fetch(BUDGET_VERWALTUNG_REST_API_URL + '/getChartDto/' + this.chartItemName, {method: 'GET'})
          .then(response => response.json())
          .then(data => this.chartDto = data)
          .catch(error => logger.logAndDispatchError("Fetching columnNames", error, this.$store));
    },
    refreshBudgetaryItem: function () {
      let loader = this.$loading.show({
        container: this.$refs.budgetaryContainer,
        isFullPage: false,
      });
      setTimeout(() => {
        loader.hide();
        this.budgetaryItemRefreshKey++;
      }, 2000);
    },
  },
  mounted() {
    this.fetchChartDto();
  }
}

</script>

<style scoped>
.budgetary-item {
  flex-grow: 1;
  flex-basis: 0;
}

</style>
