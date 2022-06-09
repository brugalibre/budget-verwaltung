import {BUDGET_VERWALTUNG_REST_API_URL} from '@/mixins/CommonBudgetVerwaltungRestApi';
import * as logger from '@/mixins/BudgetVerwaltungLogger';
export default {
    name: 'AquabasileaCourseBookerApi',
    methods: {
        fetchBudgetVerwaltungAppStateDto: function () {
            fetch(BUDGET_VERWALTUNG_REST_API_URL + '/getBudgetVerwaltungAppStateDto', {method: 'GET'})
                .then(async response => {
                    // retrieve the response in two steps: text -> JSON.parse. That's necessary in case we receive an empty response (which leads always to an exception)
                    const plainData = await response.text();
                    const data = await (plainData ? JSON.parse(plainData) : {});
                    // check for error response
                    if (!response.ok) {
                        // get error message from body or default to response status
                        const errorDetails = data ? data.error : 'upsidupsi, fehler nicht gefunden';
                        const errorMsg = 'Fehler beim Laden vom Server: ' + errorDetails + '!\nFehlercode ' + response.status;
                        return Promise.reject(errorMsg);
                    }
                    this.$store.dispatch('setBudgetVerwaltungAppStateDto', data)
                })
                .catch(error => logger.logAndDispatchError('Fetching BudgetVerwaltungAppStateDtos', error, this.$store));
        },
        fetchApplicationTitle: function () {
            fetch(BUDGET_VERWALTUNG_REST_API_URL + '/getApplicationTitle')
                .then(response => response.json())
                .then(data => this.$store.dispatch('setApplicationTitle', data.applicationTitle))
                .catch(error => logger.logAndDispatchError('Fetching application-title', error, this.$store));
        }
    }
}
