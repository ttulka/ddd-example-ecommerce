const template = document.createElement('template');
template.innerHTML = `
    <form action="#" method="post" class="delivery-form">
        <table>
            <tr>
                <td>Name</td>
                <td><input name="name" size="30"></td>
            </tr>
            <tr>
                <td>Address</td>
                <td><input name="address" size="50"></td>
            </tr>
        </table>
    </form>
`;
customElements.define('delivery-form', class extends HTMLElement {
    constructor() {
        super();
        this._keydownListener = e => e.target.classList.remove('error');
        this._formSubmitListener = e => e.preventDefault();
    }
    connectedCallback() {
        this.appendChild(template.content.cloneNode(true));
        
        this._formEl = this.querySelector('form');
        this._nameInp = this._formEl.querySelector('input[name=name]');
        this._addressInp = this._formEl.querySelector('input[name=address]');
        
        this._nameInp.addEventListener('keydown', this._keydownListener);
        this._addressInp.addEventListener('keydown', this._keydownListener);
        this._formEl.addEventListener('submit', this._formSubmitListener);
    } 
    disconnectedCallback() {
        this._nameInp.removeEventListener('keydown', this._keydownListener);
        this._addressInp.removeEventListener('keydown', this._keydownListener);
        this._formEl.removeEventListener('submit', this._formSubmitListener);
    }
    isValid() {
        return this._nameInp.value && this._nameInp.value.trim() &&
            this._addressInp.value && this._addressInp.value.trim();
    }
    markAsInvalid() {
        if (!(this._nameInp.value && this._nameInp.value.trim())) {
            this._nameInp.classList.add('error');
        }
        if (!(this._addressInp.value && this._addressInp.value.trim())) {
            this._addressInp.classList.add('error');
        }
    }
    submit() {
        const name = this._nameInp.value;
        const address = this._addressInp.value;
        
        this.dispatchEvent(new CustomEvent('delivery:prepare', {detail: {name, address}}));
    }
});
