import '../catalog/CategoriesMenu.js';
import '../cart/MenuLink.js';

import catalogService from '../service/catalog.js';

(function layout() {
	const categoriesMenu = document.querySelector("catalog-categories-menu");
	
	catalogService.categories()
		.then(c => categoriesMenu.categories = c);
})();