const bg = await fetch('../locales/bg.json').then(response => response.json())

const observer2 = new MutationObserver(function(mutations_list) {

    var userLang = navigator.language || navigator.userLanguage;
    if (userLang == "bg"){
        if($(".hidden-sm.hidden-xs.ng-binding").length) {
        $(".hidden-sm.hidden-xs.ng-binding").each(function( index ) {
            if ($(this).text() == bg.labels.FILTER_ADD_CRITERION) {
               $(this).closest('.form-horizontal').children().find('.col-xs-10').removeClass('col-xs-10').addClass('col-xs-9');
               $(this).closest('.form-horizontal').children().find('.col-xs-2').removeClass('col-xs-2').addClass('col-xs-3');
            }
        });}

        let placeholder = 'All tasks'
        let element = $("a.ng-binding:contains("+placeholder+")");
        element.html(element.html().replace(placeholder,bg.labels.ALL_TASKS));

        $(".match-type button").width("auto");
    }
});

observer2.observe(document.body, { subtree: true, childList: true });