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