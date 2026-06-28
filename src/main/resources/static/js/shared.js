(() => {
    if (globalThis.milvShopSharedInitialized) {
        return;
    }
    globalThis.milvShopSharedInitialized = true;

    const state = {
        paypalCheckoutWindow: null
    };

    $(document).on('htmx:afterSwap', '.modal-replaced-pulse', function () {
        const dialog = $(this).closest('.modal-dialog');
        const modal = dialog.parent();
        if (!dialog.length) return;

        // skip the pulse if the modal is still mid-opening
        if (modal.hasClass('show') === false) return;

        dialog.removeClass('modal-pulse');
        dialog[0].getBoundingClientRect();
        dialog.addClass('modal-pulse');
    });

    $(document).on('animationend', '.modal-pulse', function (e) {
        if (e.originalEvent.animationName === 'modal-pulse') {
            $(this).removeClass('modal-pulse');
        }
    });

    htmx.on("htmx:beforeRequest", function (e) {
        if ($(e.detail.elt).is("[data-paypal-checkout-launcher]")) {
            state.paypalCheckoutWindow = openPayPalCheckoutWindow();
        }
    });

    htmx.on("openPayPalCheckout", function (e) {
        const url = e.detail.value;

        if (!url) {
            closePayPalCheckoutWindow();
            return;
        }

        if (state.paypalCheckoutWindow && !state.paypalCheckoutWindow.closed) {
            state.paypalCheckoutWindow.location.href = url;
            state.paypalCheckoutWindow.focus();
        } else {
            state.paypalCheckoutWindow = openPayPalCheckoutWindow(url);
        }
    });

    htmx.on("closePayPalCheckout", closePayPalCheckoutWindow);

    function closePayPalCheckoutWindow() {
        if (state.paypalCheckoutWindow && !state.paypalCheckoutWindow.closed) {
            state.paypalCheckoutWindow.close();
        }

        state.paypalCheckoutWindow = null;
    }

    function openPayPalCheckoutWindow(url = "about:blank") {
        return window.open(url, "paypalCheckout", "popup=yes,width=1080,height=760,menubar=no,toolbar=no,location=yes,status=no,resizable=yes,scrollbars=yes");
    }
})();
