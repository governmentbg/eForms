const bg = await fetch('../locales/bg.json').then(response => response.json())
const en = await fetch('../locales/en.json').then(response => response.json())

const observer2 = new MutationObserver(function(mutations_list) {

  var userLang = navigator.language || navigator.userLanguage;
  if (userLang == "bg"){

    $("span.instances.ng-scope:contains(No)").each(function(){
        let value = $(this).text().replace('No', '0');
        $(this).text(value)
    });

    const zoomOut = document.querySelectorAll(".glyphicon.glyphicon-minus")[0];
    if (zoomOut) {
        if (zoomOut.parentElement.title == en.labels.ZOOM_OUT){
            zoomOut.parentElement.title = bg.labels.ZOOM_OUT
        }
    }
    const zoomIn = document.querySelectorAll(".glyphicon.glyphicon-plus")[0];
    if (zoomIn) {
        if (zoomIn.parentElement.title == en.labels.ZOOM_IN){
            zoomIn.parentElement.title = bg.labels.ZOOM_IN
        }
    }
    const resetZoom = document.querySelectorAll(".glyphicon.glyphicon-screenshot")[0];
    if (resetZoom) {
        if (resetZoom.parentElement.title == en.labels.RESET_ZOOM){
            resetZoom.parentElement.title = bg.labels.RESET_ZOOM
        }
    }
    const status = document.querySelectorAll(".status.ng-binding.ng-scope")[0];
    if (status){
        if (status.innerText == en.labels.ERROR){
            status.innerHTML = bg.labels.ERROR
        }
    }
    const message = document.querySelectorAll(".message.ng-binding.ng-scope")[0];
    if (message) {
        if (message.innerText == en.labels.CUSTOM_ERROR_MESSAGE) {
            message.innerHTML = bg.labels.CUSTOM_ERROR_MESSAGE
        }
    }
  }

});

observer2.observe(document.body, { subtree: false, childList: true });