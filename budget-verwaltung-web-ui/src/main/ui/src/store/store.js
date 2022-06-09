import {createStore} from 'vuex'

export const store = createStore({
    state: {
        applicationTitle: 'Budget-Verwaltung',
        globalErrorMsg: null,
        budgetVerwaltungAppStateDto: {
            stagingMsg: 'Budget-Verwaltung 2.0',
            chartNames: [],
        },
    },
    getters: {
        budgetVerwaltungAppStateDto: state => {
            return state.budgetVerwaltungAppStateDto;
        },
        applicationTitle: state => {
            return state.applicationTitle;
        },
        globalErrorMsg: state => {
            return state.globalErrorMsg;
        },
    },
    mutations: {
        setBudgetVerwaltungAppStateDto(state, budgetVerwaltungAppStateDto) {
            state.budgetVerwaltungAppStateDto = budgetVerwaltungAppStateDto;
        },
        setApplicationTitle(state, applicationTitle) {
            state.applicationTitle = applicationTitle;
        },
        setGlobalErrorMsg(state, globalErrorMsg) {
            state.globalErrorMsg = globalErrorMsg;
        },
    },
    actions: {
        setBudgetVerwaltungAppStateDto(context, budgetVerwaltungAppStateDto) {
            context.commit("setBudgetVerwaltungAppStateDto", budgetVerwaltungAppStateDto);
        },
        setApplicationTitle(context, applicationTitle) {
            context.commit("setApplicationTitle", applicationTitle);
        },
        setGlobalErrorMsg(context, globalErrorMsg) {
            context.commit("setGlobalErrorMsg", globalErrorMsg);
        },
    },
});
