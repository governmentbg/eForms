const bg = await fetch('../locales/bg.json').then(response => response.json())
const en = await fetch('../locales/en.json').then(response => response.json())
let paginationTranslated = false

const observer2 = new MutationObserver(function(mutations_list) {
    var userLang = navigator.language || navigator.userLanguage;
    if (userLang == "bg"){
        $('.confirm-delete-authorization.modal-body.ng-scope').children().find('dd').each(function( index ) {
            if ($(this).text() == en.labels.ALL) {
                $(this).text(bg.labels.ALL);
                return;
            }
            if ($(this).text() == en.labels.NONE) {
                $(this).text(bg.labels.NONE);
                return;
            }
        });

        $('.permissions.ng-binding.ng-scope:contains('+en.labels.ALL+')').text(bg.labels.ALL);
        $('.permissions.ng-binding.ng-scope:contains('+en.labels.NONE+')').text(bg.labels.NONE);

        $('.confirm-delete-authorization.modal-body.ng-scope').children().find('dt').each(function( index ) {
                    if ($(this).text() == bg.labels.AUTHORIZATION_RESOURCE_ID) {
                        $(this).text(bg.labels.AUTHORIZATION_RESOURCE_ID_SHORT);
                        return;
                    }
        });

        const app = document.querySelectorAll("h3")[0];
        if (app) {
            if (app.innerText == "Приложение Оторизации"){
                app.innerHTML = bg.labels.APPLICATION_AUTHORIZATION
            }
        }

        $('.pagination-first a:contains('+en.labels.FIRST+')').text(bg.labels.FIRST);
        $('.pagination-prev a:contains('+en.labels.PREVIOUS+')').text(bg.labels.PREVIOUS);
        $('.pagination-next a:contains('+en.labels.NEXT+')').text(bg.labels.NEXT);
        $('.pagination-last a:contains('+en.labels.LAST+')').text(bg.labels.LAST);
    }
});


observer2.observe(document.body, { subtree: true, childList: true });