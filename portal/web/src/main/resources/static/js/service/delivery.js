export default {
    prepare: (orderId, {name, address}) =>
        fetch('/delivery', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                orderId, name, address
            })
        })
            .catch(console.error)
}