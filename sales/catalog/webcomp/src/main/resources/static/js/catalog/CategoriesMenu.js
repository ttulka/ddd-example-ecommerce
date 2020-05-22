const template = document.createElement('template');
template.innerHTML = `
    <nav class="categories">
            <a href="/" class="home">&#127968;</a>
            <span class="categories"></span>
        </nav>
`;
customElements.define('catalog-categories-menu', class extends HTMLElement {
    constructor() {
        super();
        this._categories = [];
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));

        this._categoriesEl = this.querySelector('.categories');
    }
    set categories(categories) {
        this._categories = categories || [];
        this._renderCategories();
    }
    _renderCategories() {
        this._categories.forEach(({uri, title}) => this._categoriesEl.innerHTML += 
            `<a href="/category/${uri}">${title}</a>`);
    }
});
