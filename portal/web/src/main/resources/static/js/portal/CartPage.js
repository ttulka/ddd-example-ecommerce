import '../cart/ItemList.js';

import cartService from '../service/cart.js';

import {register} from './page.js';

const pageName = 'portal-cart-page';

register(pageName, '/cart');

const template = document.createElement('template');
template.innerHTML = `
    <h1>Shopping Cart</h1>
    <div class="cart">
        <cart-item-list></cart-item-list>
        <div class="order">
            <a href="/order">Place Order</a>
        </div>
    </div>
`;
customElements.define(pageName, class extends HTMLElement {
    constructor() {
        super();
        this._itemsCount = 0;
        this._removeItemListener = ({detail: {productId}}) => this._removeItem(productId);
    }
    connectedCallback() {
        console.debug('Cart page initialized');        
        
        this.appendChild(template.content.cloneNode(true));         
        this._itemList = this.querySelector('cart-item-list');  
        this._placeOrder = this.querySelector('.order');

        this._itemList.addEventListener('cart:remove', this._removeItemListener);
                
        this._togglePlaceOrder();
        this._loadItems();
    }
    disconnectedCallback() {
        this._itemList.removeEventListener('cart:remove', this._removeItemListener);
    }
    _loadItems() {
        cartService.items()
            .then(items => {
                this._itemList.items = items;

                this._itemsCount = items.length;
                this._togglePlaceOrder();
            });
    }
    _removeItem(productId) {
        cartService.removeItem(productId)
            .then(_ => window.dispatchEvent(new CustomEvent('cart:removed', {detail: {productId}})));

        this._itemsCount--;
        this._togglePlaceOrder();
    }
    _togglePlaceOrder() {
        if (this._itemsCount) {
            this._placeOrder.style.display = '';
        } else {
            this._placeOrder.style.display = 'none';
        }
    }
});