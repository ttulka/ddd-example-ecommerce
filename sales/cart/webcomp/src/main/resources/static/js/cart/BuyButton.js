const template = document.createElement('template');
template.innerHTML = `
    <button type="button" class="buy">buy</button>
`;
customElements.define('cart-buy-button', class extends HTMLElement {
    constructor() {
        super();
        this._buttonListener = e => {
            this._addIntoCart(
                this.getAttribute('productId'),
                this.getAttribute('title'),
                parseFloat(this.getAttribute('price')),
                1
            );
            e.preventDefault();
        }
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));
        
        this._button = this.querySelector('.buy');
        this._button.addEventListener('click', this._buttonListener);

    }
    disconnectedCallback() {
        this._button.removeEventListener('click', this._buttonListener);
    }
    _addIntoCart(productId, title, price, quantity) {
        this.dispatchEvent(new CustomEvent('cart:add', {detail: {productId, title, price, quantity}, bubbles: true}));
    }
});
