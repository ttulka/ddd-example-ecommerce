export default {
    products: () => 
        fetch('/catalog/products')
            .then(b => b.json())
            .catch(console.error),
        
    productsFromCategory: uri => 
        fetch(`/catalog/products/${uri}`)
            .then(b => b.json())
            .catch(console.error),

    categories: () =>
    	fetch('/catalog/categories')
            .then(b => b.json())
            .catch(console.error)
}
