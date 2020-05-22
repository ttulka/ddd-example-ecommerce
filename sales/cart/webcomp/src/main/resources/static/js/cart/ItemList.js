const template = document.createElement('template');
template.innerHTML = `
    <table class="items" cellpadding="10" cellspacing="0" border="0">
    </table>
`;
customElements.define('cart-item-list', class extends HTMLElement {
    constructor() {
        super();
        this._items = [];
        this._listeners = [];
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));
        this._itemsEl = this.querySelector('.items');
    }
    disconnectedCallback() {
        this._removeListeners();
    }
    set items(items) {
        console.debug('cart items set', items);
        this._items = items;
        this._removeListeners();
        this._render();
    }
    _removeListeners() {
        this._listeners.forEach(({el, l}) => el.removeEventListener('click', l));
        this._listeners = [];
    }
    _render(html) {
        const formatPrice = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
        }).format;
        
        this._items.map(({productId, title, price, quantity}) => {
            const template = document.createElement('template');
            template.innerHTML = `
                <tr class="item">
                    <td class="title">${title}</td>
                    <td class="price">${formatPrice(price)}</td>
                    <td class="quantity">${quantity}</td>
                    <td>
                        <span class="remove">&#x2718;</span>
                    </td>
                </tr>`;
            this._itemsEl.appendChild(template.content);
            const el = this._itemsEl.lastChild;
                
            const removeListener = e => {
                e.preventDefault();
                this._items = this._items.reduce((a, c) => productId === c.productId ? a : [...a, c], []);
                el.innerHTML = '';
                this.dispatchEvent(new CustomEvent('cart:remove', {detail: {productId}}));
            };
            const removeEl = el.querySelector('.remove');
            removeEl.addEventListener('click', removeListener);
            this._listeners.push({el: removeEl, l: removeListener});
        });
    }
});
