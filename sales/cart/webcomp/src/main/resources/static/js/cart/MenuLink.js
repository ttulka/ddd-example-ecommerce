const template = document.createElement('template');
template.innerHTML = `
    <a href="/cart" class="cart">&#x1F6D2; <span class="amount"></span></a>
`;
customElements.define('cart-menu-link', class extends HTMLElement {
    constructor() {
        super();        
        this._products = new Set();
        this._addItemListener = ({detail:{productId}}) => this.addItem(productId);
        this._removeItemListener = ({detail:{productId}}) => this.removeItem(productId);
        this._emptyCartListener = e => this.emptyCart();
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));

        this._amountEl = this.querySelector('.amount');
        
        window.addEventListener('cart:added', this._addItemListener);
        window.addEventListener('cart:removed', this._removeItemListener);
        window.addEventListener('cart:emptied', this._emptyCartListener);
    }
    disconnectedCallback() {        
        window.removeEventListener('cart:added', this._addItemListener);
        window.removeEventListener('cart:removed', this._removeItemListener);
        window.removeEventListener('cart:emptied', this._emptyCartListener);
    }
    set amount(amount) {
        console.debug('set amount', amount);
        this._amount = amount;
        this._amountEl.innerHTML = amount ? amount : '';
    }
    addItem(productId) {
        this._products.add(productId);
        this._updateAmount();
    }
    removeItem(productId) {
        this._products.delete(productId);
        this._updateAmount();
    }
    emptyCart() {
        this._products.clear();
        this._updateAmount();
    }
    _updateAmount() {
        this.amount = this._products.size; 
    }
});
