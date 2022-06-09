<template>
  <div>
    <h4>Neuer Wert hinzufügen</h4>
    <div class="grid-container">
      <div style="display: flex; justify-content: space-between">
        <multiselect
            id="monthSelector"
            mode="single"
            v-model="selectedColumnDto"
            :options="columnDtos"
            :label="'name'"
            :valueProp="'name'"
            noOptionsText="Keine Daten verfügbar"
            :placeholder="'Spalte auswählen..'"
            :object=true
        >
          <template v-slot:option="{ option }">
            <label>{{ option.name }}</label>
          </template>
        </multiselect>
        <CFormInput
            style="margin-left: 10px; max-width: 150px"
            :disabled="!this.selectedColumnDto"
            id="newValueInput"
            v-model="newValue"
            type="number"
            name="newValue"
            placeholder="Wert eingeben"
            @change="onChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import BudgetVerwaltungApi from "@/mixins/BudgetVerwaltungApi";
import {BUDGET_VERWALTUNG_REST_API_URL} from "@/mixins/CommonBudgetVerwaltungRestApi";
import * as logger from "@/mixins/BudgetVerwaltungLogger";

export default {
  name: 'BudgetaryItemAddValue',
  props: ['chartItemName', 'columnDtos'],
  mixins: [BudgetVerwaltungApi],
  data() {
    return {
      selectedColumnDto: '',
      newValue: '',
    }
  },
  watch: {
    columnDtos: {
      handler() {
        this.selectCurrentSelectedColumn();
      }
    }
  },
  methods: {
    addCellValue: function (chartName, columnName, value) {
      const url = BUDGET_VERWALTUNG_REST_API_URL + '/addCellValue/' + chartName + '/?columnName=' + columnName + '&value=' + value;
      fetch(url, {method: 'POST'})
          .then(response => console.log('Response: ' + JSON.stringify(response)))
          .catch(error => logger.logAndDispatchError('Adding new value ' + value , error, this.$store));
    },
    selectCurrentSelectedColumn: function () {
      this.selectedColumnDto = this.columnDtos.find(columnDto => columnDto.isSelected);
    },
    onChange: function () {
      this.addCellValue(this.chartItemName, this.selectedColumnDto.key, this.newValue);
      this.newValue = '';
      this.selectCurrentSelectedColumn();
      this.$emit('refreshBudgetaryItem');
    }
  },
  mounted() {
    this.selectCurrentSelectedColumn();
  }
}

</script>

<style src="@vueform/multiselect/themes/default.css">

</style>
