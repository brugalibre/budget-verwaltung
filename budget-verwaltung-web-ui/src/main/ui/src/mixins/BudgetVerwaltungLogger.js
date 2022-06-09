export function logAndDispatchError(context, errorMsg, store) {
    console.error(context + ': ' + errorMsg);
    store.dispatch('setGlobalErrorMsg', errorMsg);
}
