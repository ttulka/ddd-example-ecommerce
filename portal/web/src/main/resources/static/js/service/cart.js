export default {
    items: () =>
        fetch('/cart')
            .then(b => b.json())
            .catch(console.error),
    
    addItem: item =>
        fetch('/cart', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(item)
        })
            .catch(console.error),

    removeItem: productId =>
        fetch('/cart?productId=' + productId, {
            method: 'DELETE'
        })
            .catch(console.error),
    
    empty: () =>
        fetch('/cart/empty', {
            method: 'POST'
        })
            .catch(console.error)
}