export default {
    place: (orderId, items, total) =>
        fetch('/order', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                orderId, items, total
            })
        })
            .catch(console.error)
}
