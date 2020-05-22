import './portal/Layout.js';
import './portal/CatalogPage.js';
import './portal/CartPage.js';
import './portal/OrderPage.js';

(function application() {
    const appContent = document.querySelector("#content");

    const registeredPages = [];
        
    function createComponent(path) {
        const c = registeredPages.find(r => r.matches(path)) 
            || {component: path => document.createTextNode('not found: ' + path)};
        return c.component(path);
    }
    function showComponent(component) {
        if (appContent.firstChild) {
            appContent.replaceChild(component, appContent.firstChild);
        } else {
            appContent.appendChild(component);
        }
    }
    function loadComponent(path) {
        showComponent(createComponent(path));
     }
     function navigateTo(href) {
        history.pushState({id: Date.now()}, href, href);
        loadComponent(href);
     }
     function register(matches, component) {
        registeredPages.push({matches, component});
        const path = window.location.pathname;
        if (matches(path)) {
            showComponent(component(path));
        }
     }
     document.addEventListener('click', e => {
        if (e.target.nodeName === 'A') {
            navigateTo(e.target.getAttribute('href'));
            e.preventDefault();
        }
     });
     window.addEventListener('page:nav', ({detail: {href}}) => navigateTo(href));
     window.addEventListener('page:register', ({detail: {matches, component}}) => register(matches, component));

     window.dispatchEvent(new CustomEvent('application:ready'));
})();
