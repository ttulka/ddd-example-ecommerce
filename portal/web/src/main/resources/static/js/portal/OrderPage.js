import '../order/OrderForm.js';
import '../delivery/DeliveryForm.js';

import orderService from '../service/order.js';
import deliveryService from '../service/delivery.js';
import cartService from '../service/cart.js';

import {register} from './page.js';

const pageName = 'portal-order-page';

register(pageName, '/order');

const template = document.createElement('template');
template.innerHTML = `
    <h1>Place Order</h1>
    <div class="order">
        <delivery-form></delivery-form>
        <order-form></order-form>
    </div>
`;
customElements.define(pageName, class extends HTMLElement {
    constructor() {
        super();
        this._orderId = Date.now();
        
        this._orderPlaceListener = ({detail: {items}}) => {
            if (this._deliveryForm.isValid()) {
                this._placeOrder(this._orderId, items);
                this._deliveryForm.submit();
            } else {
                this._deliveryForm.markAsInvalid();
            }
        }
        this._deliveryPrepareListener = ({detail}) => this._prepareDelivery(this._orderId, detail);
    }
    connectedCallback() {
        console.debug('Order page initialized');        
        
        this.appendChild(template.content.cloneNode(true));  
        
        this._orderForm = this.querySelector('order-form');
        this._deliveryForm = this.querySelector('delivery-form');
        this._orderEl = this.querySelector('.order');
        
        this._loadItems();
        
        this._orderForm.addEventListener('order:place', this._orderPlaceListener);
        this._deliveryForm.addEventListener('delivery:prepare', this._deliveryPrepareListener);
    }
    disconnectedCallback() {
        this._orderForm.removeEventListener('order:place', this._orderPlaceListener);
        this._deliveryForm.removeEventListener('delivery:prepare', this._deliveryPrepareListener);
    }
    _loadItems() {
        cartService.items()
            .then(items => this._orderForm.items = items);
    }
    _placeOrder(orderId, items) {
        const total = items.reduce((acc, i) => acc + i.price * i.quantity, 0.0);
        orderService.place(orderId, items, total)
            .then(_ => {
                window.dispatchEvent(new CustomEvent('order:placed', {detail: {orderId}}));

                cartService.empty()
                    .then(_ => window.dispatchEvent(new CustomEvent('cart:emptied')));
            })
            .then(_ => console.debug('Order placed', orderId, items));
        this._orderEl.innerHTML = '<p class="success">Order has been successfully created.</p>';
    }
    _prepareDelivery(orderId, delivery) {
        deliveryService.prepare(orderId, delivery)
            .then(_ => console.debug('Delivery prepared', orderId, delivery));
    }
});
