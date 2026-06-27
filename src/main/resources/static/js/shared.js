$(document).on('htmx:afterSwap', '.modal-replaced-pulse', function () {
    const dialog = $(this).closest('.modal-dialog');
    const modal = dialog.parent();
    if (!dialog.length) return;

    // skip the pulse if the modal is still mid-opening
    if (modal.hasClass('show') === false) return;

    dialog.removeClass('modal-pulse');
    void dialog[0].offsetWidth; // force reflow so animation can replay
    dialog.addClass('modal-pulse');
});

$(document).on('animationend', '.modal-pulse', function (e) {
    if (e.originalEvent.animationName === 'modal-pulse') {
        $(this).removeClass('modal-pulse');
    }
});

let paypalCheckoutWindow = null;

htmx.on("htmx:beforeRequest", function (e) {
    if ($(e.detail.elt).is("[data-paypal-checkout-launcher]")) {
        paypalCheckoutWindow = openPayPalCheckoutWindow();
    }
});

htmx.on("openPayPalCheckout", function (e) {
    const url = e.detail.value;

    if (!url) {
        closePayPalCheckoutWindow();
        return;
    }

    if (paypalCheckoutWindow && !paypalCheckoutWindow.closed) {
        paypalCheckoutWindow.location.href = url;
        paypalCheckoutWindow.focus();
    } else {
        paypalCheckoutWindow = openPayPalCheckoutWindow(url);
    }
});

htmx.on("closePayPalCheckout", closePayPalCheckoutWindow);

function closePayPalCheckoutWindow() {
    if (paypalCheckoutWindow && !paypalCheckoutWindow.closed) {
        paypalCheckoutWindow.close();
    }

    paypalCheckoutWindow = null;
}

function openPayPalCheckoutWindow(url = "about:blank") {
    return window.open(url, "paypalCheckout", "popup=yes,width=1080,height=760,menubar=no,toolbar=no,location=yes,status=no,resizable=yes,scrollbars=yes");
}
