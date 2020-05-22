export default {
    inStock: productIds => 
        fetch('/warehouse/stock', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(productIds)
        })
            .then(b => b.json())
            .catch(console.error)
}