export function register(name, matches, setup = null) {
    console.debug('Registering page', name);

    const readyListener = e => registerPage();
    function registerPage() {
        window.dispatchEvent(new CustomEvent('page:register', {detail: {
            matches: typeof matches === 'string' 
                ? path => path === matches 
                : matches, 
            component: path => {
                const c = document.createElement(name);
                if (setup) {
                    setup(c, path);
                }
                return c;
            }
        }}));
        window.removeEventListener('application:ready', readyListener);
    }
    window.addEventListener('application:ready', readyListener);
}