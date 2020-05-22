const template = document.createElement('template');
template.innerHTML = `
    <table class="products" cellpadding="10" cellspacing="0" border="0">
    </table>
`;
customElements.define('catalog-product-list', class extends HTMLElement {
    constructor() {
        super();
        this._products = [];
        this._inStock = [];
        this._listeners = [];
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));
        this._productsEl = this.querySelector('.products');
    }
    disconnectedCallback() {
        this._removeListeners();
    }
    set products(products) {
        console.debug('products set', products);
        this._products = products || [];
        this._render();
    }
    set stock(stock) {
        console.debug('stock set', stock);
        this._inStock = stock.reduce((a, {productId, inStock}) => ({...a, [productId]: inStock}), {});
        this._renderInStock();
    }
    _removeListeners() {
        this._listeners.forEach(({el, l}) => el.removeEventListener('click', l));
        this._listeners = [];
    }
    _render() {
        const formatPrice = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
        }).format;
        
        this._products.map(({id, title, description, price}) => {
            const template = document.createElement('template');
            template.innerHTML = `
                <tr class="product" id="product-${id}">
                    <th>${title}</th>
                    <td>${description}</td>
                    <td class="stock">${this._inStockValue(id)}</td>
                    <td class="price">${formatPrice(price)}</td>
                    <td><cart-buy-button 
                            productId="${id}" 
                            title="${title}" 
                            price="${price}">
                        </cart-buy-button>
                    </td>
                </tr>`;
            this._productsEl.appendChild(template.content);
        });
    }
    _renderInStock() {
        this._products.forEach(({id}) => {
            const stockEl = this._productsEl.querySelector('#product-' + id + ' .stock');
            if (stockEl) {
                stockEl.innerHTML = this._inStockValue(id);
            }
        });
    }
    _inStockValue(productId){
        return this._inStock[productId] !== undefined
            ? this._inStock[productId] > 0 ? this._inStock[productId] + ' left in stock' : 'sold out'
            : '...';
    }
});
