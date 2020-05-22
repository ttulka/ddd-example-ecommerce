const template = document.createElement('template');
template.innerHTML = `
    <form action="#" method="post" class="order-form">
        <ul class="items"></ul>
        <p>
            <button type="submit" disabled="disabled">Submit</button>
        </p>
    </form>
`;
customElements.define('order-form', class extends HTMLElement {
    constructor() {
        super();
        this._items = [];
        this._formSubmitListener = e => {
            this._placeOrder();
            e.preventDefault();
        };
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));
        
        this._itemsEl = this.querySelector('.items');
        this._formEl = this.querySelector('form');
        this._submitButton = this._formEl.querySelector('button');

        this._formEl.addEventListener('submit', this._formSubmitListener);
    }
    disconnectedCallback() {
        this._formEl.removeEventListener('submit', this._formSubmitListener);
    }
    set items(items) {
        console.debug('order items set', items);
        this._items = items;
        this._renderItems();

        if (items && items.length) {
            this._submitButton.removeAttribute('disabled');
        }        
    }
    _placeOrder() {
        this.dispatchEvent(new CustomEvent('order:place', {detail: {items: this._items}}));
    }
    _renderItems() {
        this._items.forEach(({title, quantity}) => this._itemsEl.innerHTML += `<li><b>${quantity}x</b> ${title}</li>`);
    }
});
