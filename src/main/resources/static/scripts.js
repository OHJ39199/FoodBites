/**
 * Scripts para interactuar con la API de Truck Bites.
 */
const API_BASE_URL = 'http://localhost:8080/api';
const PLACEHOLDER_IMAGE = '/img/placeholder.jpg';

// Mostrar mensaje de error en un elemento
function showError(elementId, message) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `<p class="text-danger text-center">${message}</p>`;
    }
}

// Cargar contenido al iniciar la página
document.addEventListener('DOMContentLoaded', () => {
    // Cargar food trucks cercanos y carrusel en index.html
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const ciudad = document.getElementById('ciudad').value;
            const calle = document.getElementById('calle').value;
            loadFoodTrucksCercanos(ciudad, calle);
        });
        loadFoodTrucksCercanos('Nueva York', '');
        loadCarouselFoodTrucks();
    }

    // Cargar todos los food trucks en foodtrucks.html
    if (window.location.pathname.includes('foodtrucks.html')) {
        loadAllFoodTrucks();
    }

    // Cargar menús en menu.html
    if (window.location.pathname.includes('menu.html')) {
        const urlParams = new URLSearchParams(window.location.search);
        const foodTruckId = urlParams.get('foodTruckId');
        const foodTruckName = urlParams.get('foodTruckName');
        if (foodTruckId && foodTruckName) {
            document.getElementById('menuTitle').textContent = `Menú de ${foodTruckName}`;
            loadMenus(foodTruckId);
        } else {
            document.getElementById('menuTitle').textContent = 'Todos los Menús';
            loadAllMenus();
        }
    }

    // Cargar menús y manejar pedido en pedido.html
    if (window.location.pathname.includes('pedido.html')) {
        const urlParams = new URLSearchParams(window.location.search);
        const foodTruckId = urlParams.get('foodTruckId');
        if (foodTruckId) {
            document.getElementById('foodTruckId').value = foodTruckId;
            loadMenusForPedido(foodTruckId);
        }
        const pedidoForm = document.getElementById('pedidoForm');
        if (pedidoForm) {
            pedidoForm.addEventListener('submit', (e) => {
                e.preventDefault();
                submitPedido();
            });
        }
    }

    // Manejar creación, modificación y eliminación de menús en adminMenu.html
    const createMenuForm = document.getElementById('createMenuForm');
    if (createMenuForm) {
        createMenuForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createMenu();
        });
    }

    const createMenusForm = document.getElementById('createMenusForm');
    if (createMenusForm) {
        createMenusForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createMenus();
        });
    }

    const updateMenuForm = document.getElementById('updateMenuForm');
    if (updateMenuForm) {
        updateMenuForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await updateMenu();
        });
    }

    const deleteMenuForm = document.getElementById('deleteMenuForm');
    if (deleteMenuForm) {
        deleteMenuForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await deleteMenu();
        });
    }

    // Manejar creación, modificación y eliminación de food trucks en adminFoodTrucks.html
    const createFoodTruckForm = document.getElementById('createFoodTruckForm');
    if (createFoodTruckForm) {
        createFoodTruckForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createFoodTruck();
        });
    }

    const updateFoodTruckForm = document.getElementById('updateFoodTruckForm');
    if (updateFoodTruckForm) {
        updateFoodTruckForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await updateFoodTruck();
        });
    }

    const deleteFoodTruckForm = document.getElementById('deleteFoodTruckForm');
    if (deleteFoodTruckForm) {
        deleteFoodTruckForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await deleteFoodTruck();
        });
    }

    // Manejar promedio de beneficio en adminFoodTrucks.html
    const averageProfitForm = document.getElementById('averageProfitForm');
    if (averageProfitForm) {
        averageProfitForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const foodTruckId = document.getElementById('foodTruckIdProfit').value.trim();
            if (!foodTruckId || isNaN(foodTruckId) || parseInt(foodTruckId) <= 0) {
                showError('topFoodTrucksList', 'Por favor, introduce un ID válido.');
                return;
            }
            await showAverageProfit(foodTruckId);
        });
    }

    // Manejar registro en register.html
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await register();
        });
    }
});

// Mostrar el beneficio medio de un food truck
async function showAverageProfit(foodTruckId) {
    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks/${foodTruckId}/average-profit`);
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Error al obtener el beneficio medio');
        }
        const averageProfit = await response.json();
        const topFoodTrucksList = document.getElementById('topFoodTrucksList');
        topFoodTrucksList.innerHTML = '';

        // Fetch food truck details for name
        const truckResponse = await fetch(`${API_BASE_URL}/foodtrucks/${foodTruckId}`);
        if (!truckResponse.ok) {
            throw new Error('Error al obtener detalles del food truck');
        }
        const truck = await truckResponse.json();

        const item = `
            <div class="list-group-item">
                <h5>Food Truck: ${truck.nombre}</h5>
                <p><small>Beneficio Medio por Pedido: $${averageProfit.toFixed(2)}</small></p>
            </div>
        `;
        topFoodTrucksList.innerHTML = item;
    } catch (error) {
        console.error('Error al obtener beneficio medio:', error);
        showError('topFoodTrucksList', error.message);
    }
}

// Cargar top 3 food trucks por número de pedidos
async function listTopFoodTrucks() {
    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks/top-by-orders?limit=3`);
        if (!response.ok) {
            throw new Error('Error al cargar los food trucks más populares');
        }
        const foodTrucks = await response.json();
        const topFoodTrucksList = document.getElementById('topFoodTrucksList');
        topFoodTrucksList.innerHTML = '';
        if (foodTrucks.length === 0) {
            topFoodTrucksList.innerHTML = '<p class="text-center">No se encontraron food trucks con pedidos.</p>';
            return;
        }
        foodTrucks.forEach(truck => {
            const item = `
                <div class="list-group-item">
                    <h5>${truck.nombre}</h5>
                    <p><small>Cocina: ${truck.tipoCocina} | Ubicación: ${truck.ubicacionActual} | Pedidos: ${truck.orderCount}</small></p>
                </div>
            `;
            topFoodTrucksList.innerHTML += item;
        });
    } catch (error) {
        console.error('Error al cargar top food trucks:', error);
        showError('topFoodTrucksList', 'Error al cargar los food trucks más populares.');
    }
}

// ... (rest of the functions remain unchanged: loadCarouselFoodTrucks, loadAllFoodTrucks, etc.)
async function loadCarouselFoodTrucks() {
    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks`);
        if (!response.ok) {
            throw new Error('Error al cargar food trucks para el carrusel');
        }
        const foodTrucks = await response.json();
        const carouselItems = document.getElementById('carouselItems');
        carouselItems.innerHTML = '';
        if (foodTrucks.length === 0) {
            carouselItems.innerHTML = '<div class="carousel-item"><p class="text-center">No se encontraron food trucks.</p></div>';
            return;
        }
        foodTrucks.forEach((truck, index) => {
            const isActive = index === 0 ? 'active' : '';
            const slide = `
                <div class="carousel-item ${isActive}">
                    <img src="/img/foodtruck_placeholder.jpg" class="d-block w-100" alt="${truck.nombre}">
                    <div class="carousel-caption d-none d-md-block">
                        <h3>${truck.nombre}</h3>
                        <p>Cocina: ${truck.tipoCocina}</p>
                        <p>Ubicación: ${truck.ubicacionActual}</p>
                        <a href="menu.html?foodTruckId=${truck.id}&foodTruckName=${encodeURIComponent(truck.nombre)}" class="btn btn-primary">Ver Menú</a>
                    </div>
                </div>
            `;
            carouselItems.innerHTML += slide;
        });
    } catch (error) {
        console.error('Error al cargar food trucks para el carrusel:', error);
        const carouselItems = document.getElementById('carouselItems');
        carouselItems.innerHTML = '<div class="carousel-item"><p class="text-danger text-center">Error al cargar el carrusel.</p></div>';
    }
}

async function loadAllFoodTrucks() {
    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks`);
        if (!response.ok) {
            throw new Error('Error al cargar food trucks');
        }
        const foodTrucks = await response.json();
        const foodTrucksList = document.getElementById('foodTrucksList');
        foodTrucksList.innerHTML = '';
        if (foodTrucks.length === 0) {
            foodTrucksList.innerHTML = '<p class="text-center">No se encontraron food trucks.</p>';
            return;
        }
        foodTrucks.forEach(truck => {
            const card = `
                <div class="col">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${truck.nombre}</h5>
                            <p class="card-text">Cocina: ${truck.tipoCocina}</p>
                            <p class="card-text">Ubicación: ${truck.ubicacionActual}</p>
                            <a href="menu.html?foodTruckId=${truck.id}&foodTruckName=${encodeURIComponent(truck.nombre)}" class="btn btn-primary">Ver Menú</a>
                        </div>
                    </div>
                </div>
            `;
            foodTrucksList.innerHTML += card;
        });
    } catch (error) {
        console.error('Error al cargar food trucks:', error);
        const foodTrucksList = document.getElementById('foodTrucksList');
        foodTrucksList.innerHTML = '<p class="text-danger text-center">Error al cargar los food trucks.</p>';
    }
}

async function loadFoodTrucksCercanos(ciudad, calle) {
    try {
        const url = new URL(`${API_BASE_URL}/foodtrucks/cerca`);
        url.searchParams.append('ciudad', ciudad);
        if (calle) url.searchParams.append('calle', calle);
        const response = await fetch(url);
        const foodTrucks = await response.json();
        const foodTrucksList = document.getElementById('foodTrucksList');
        foodTrucksList.innerHTML = '';
        if (foodTrucks.length === 0) {
            foodTrucksList.innerHTML = '<p class="text-center">No se encontraron food trucks cercanos.</p>';
            return;
        }
        foodTrucks.forEach(truck => {
            const card = `
                <div class="col">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${truck.nombre}</h5>
                            <p class="card-text">Cocina: ${truck.tipoCocina}</p>
                            <p class="card-text">Ubicación: ${truck.ubicacionActual}</p>
                            <a href="menu.html?foodTruckId=${truck.id}&foodTruckName=${encodeURIComponent(truck.nombre)}" class="btn btn-primary">Ver Menú</a>
                        </div>
                    </div>
                </div>
            `;
            foodTrucksList.innerHTML += card;
        });
    } catch (error) {
        console.error('Error al cargar food trucks cercanos:', error);
        const foodTrucksList = document.getElementById('foodTrucksList');
        foodTrucksList.innerHTML = '<p class="text-danger text-center">Error al cargar los food trucks cercanos.</p>';
    }
}

async function loadMenus(foodTruckId) {
    try {
        const response = await fetch(`${API_BASE_URL}/menus/foodtruck/${foodTruckId}`);
        if (!response.ok) {
            throw new Error('Error al cargar menús');
        }
        const menus = await response.json();
        const menuList = document.getElementById('menuList');
        menuList.innerHTML = '';
        if (menus.length === 0) {
            menuList.innerHTML = '<p class="text-center">No se encontraron menús para este food truck.</p>';
            return;
        }
        menus.forEach(menu => {
            const card = `
                <div class="col">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${menu.nombre}</h5>
                            <p class="card-text">${menu.descripcion}</p>
                            <p class="card-text"><strong>Precio:</strong> $${menu.precio.toFixed(2)}</p>
                        </div>
                    </div>
                </div>
            `;
            menuList.innerHTML += card;
        });
    } catch (error) {
        console.error('Error al cargar menús:', error);
        const menuList = document.getElementById('menuList');
        menuList.innerHTML = '<p class="text-danger text-center">Error al cargar los menús.</p>';
    }
}

async function loadAllMenus() {
    try {
        const response = await fetch(`${API_BASE_URL}/menus`);
        if (!response.ok) {
            throw new Error('Error al cargar menús');
        }
        const menus = await response.json();
        const menuList = document.getElementById('menuList');
        menuList.innerHTML = '';
        if (menus.length === 0) {
            menuList.innerHTML = '<p class="text-center">No se encontraron menús.</p>';
            return;
        }
        menus.forEach(menu => {
            const card = `
                <div class="col">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${menu.nombre}</h5>
                            <p class="card-text">${menu.descripcion}</p>
                            <p class="card-text"><strong>Precio:</strong> $${menu.precio.toFixed(2)}</p>
                            <p class="card-text"><strong>Food Truck:</strong> ${menu.foodTruckNombre}</p>
                        </div>
                    </div>
                </div>
            `;
            menuList.innerHTML += card;
        });
    } catch (error) {
        console.error('Error al cargar menús:', error);
        const menuList = document.getElementById('menuList');
        menuList.innerHTML = '<p class="text-danger text-center">Error al cargar los menús.</p>';
    }
}

async function loadMenusForPedido(foodTruckId) {
    try {
        const response = await fetch(`${API_BASE_URL}/menus/foodtruck/${foodTruckId}`);
        const menus = await response.json();
        const menuItems = document.getElementById('menuItems');
        menuItems.innerHTML = '';
        menus.forEach(menu => {
            const item = `
                <div class="list-group-item d-flex align-items-center">
                    <div>
                        <input type="checkbox" class="form-check-input me-2" id="menu-${menu.id}" value="${menu.nombre}" data-precio="${menu.precio}">
                        <label class="form-check-label" for="menu-${menu.id}">${menu.nombre} ($${menu.precio.toFixed(2)})</label>
                    </div>
                </div>
            `;
            menuItems.innerHTML += item;
        });
        // Actualizar ítems y monto total al seleccionar
        menuItems.addEventListener('change', updatePedido);
    } catch (error) {
        console.error('Error al cargar menús para pedido:', error);
    }
}

function updatePedido() {
    const checkboxes = document.querySelectorAll('#menuItems input:checked');
    const items = Array.from(checkboxes).map(cb => cb.value).join(', ');
    const montoTotal = Array.from(checkboxes).reduce((sum, cb) => sum + parseFloat(cb.dataset.precio), 0);
    document.getElementById('items').value = items;
    document.getElementById('montoTotal').value = montoTotal.toFixed(2);
}

async function submitPedido() {
    const pedido = {
        usuarioId: parseInt(document.getElementById('usuarioId').value),
        foodTruckId: document.getElementById('foodTruckId').value,
        items: document.getElementById('items').value,
        montoTotal: parseFloat(document.getElementById('montoTotal').value),
        estado: 'PENDIENTE'
    };
    try {
        const response = await fetch(`${API_BASE_URL}/pedidos`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pedido)
        });
        if (response.ok) {
            alert('Pedido enviado con éxito');
            document.getElementById('pedidoForm').reset();
            document.getElementById('menuItems').innerHTML = '';
        } else {
            alert('Error al enviar el pedido');
        }
    } catch (error) {
        console.error('Error al enviar pedido:', error);
        alert('Error al enviar el pedido');
    }
}

async function loadNotificaciones() {
    const usuarioId = document.getElementById('usuarioIdNotificaciones').value;
    if (!usuarioId) {
        document.getElementById('notificacionesList').innerHTML = '<p class="text-danger text-center">Por favor, ingresa un ID de usuario.</p>';
        return;
    }
    try {
        const response = await fetch(`${API_BASE_URL}/notificaciones/usuario/${usuarioId}`);
        if (!response.ok) {
            throw new Error('Error al cargar notificaciones');
        }
        const notificaciones = await response.json();
        const notificacionesList = document.getElementById('notificacionesList');
        notificacionesList.innerHTML = '';
        if (notificaciones.length === 0) {
            notificacionesList.innerHTML = '<p class="text-center">No se encontraron notificaciones.</p>';
            return;
        }
        notificaciones.forEach(notificacion => {
            const item = `
                <div class="list-group-item">
                    <h5>${notificacion.mensaje}</h5>
                    <p><small>Enviado: ${new Date(notificacion.fechaEnvio).toLocaleString()}</small></p>
                </div>
            `;
            notificacionesList.innerHTML += item;
        });
    } catch (error) {
        console.error('Error al cargar notificaciones:', error);
        document.getElementById('notificacionesList').innerHTML = '<p class="text-danger text-center">Error al cargar las notificaciones.</p>';
    }
}

async function loadPedidos() {
    const usuarioId = document.getElementById('usuarioIdPedidos').value;
    if (!usuarioId) {
        document.getElementById('pedidosList').innerHTML = '<p class="text-danger text-center">Por favor, ingresa un ID de usuario.</p>';
        return;
    }
    try {
        const response = await fetch(`${API_BASE_URL}/pedidos/usuario/${usuarioId}`);
        if (!response.ok) {
            throw new Error('Error al cargar pedidos');
        }
        const pedidos = await response.json();
        const pedidosList = document.getElementById('pedidosList');
        pedidosList.innerHTML = '';
        if (pedidos.length === 0) {
            pedidosList.innerHTML = '<p class="text-center">No se encontraron pedidos para este usuario.</p>';
            return;
        }
        pedidos.forEach(pedido => {
            const item = `
                <div class="list-group-item">
                    <h5>Pedido #${pedido.id} - ${pedido.foodTruckNombre}</h5>
                    <p class="mb-1">Ítems: ${pedido.items}</p>
                    <p class="mb-1">Monto Total: $${pedido.montoTotal.toFixed(2)}</p>
                    <p class="mb-1">Estado: ${pedido.estado}</p>
                    <p><small>Fecha: ${new Date(pedido.fechaCreacion).toLocaleString()}</small></p>
                </div>
            `;
            pedidosList.innerHTML += item;
        });
    } catch (error) {
        console.error('Error al cargar pedidos:', error);
        document.getElementById('pedidosList').innerHTML = '<p class="text-danger text-center">Error al cargar los pedidos.</p>';
    }
}

async function createMenu() {
    const foodTruckId = document.getElementById('foodTruckId').value.trim();
    const nombre = document.getElementById('nombre').value.trim();
    const descripcion = document.getElementById('descripcion').value.trim();
    const precio = document.getElementById('precio').value.trim();

    // Validate required fields
    if (!foodTruckId || isNaN(parseInt(foodTruckId)) || parseInt(foodTruckId) <= 0) {
        document.getElementById('createMenuMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un ID de Food Truck válido (número positivo).</p>';
        return;
    }
    if (!nombre) {
        document.getElementById('createMenuMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un nombre para el menú.</p>';
        return;
    }
    if (!precio || isNaN(parseFloat(precio)) || parseFloat(precio) <= 0) {
        document.getElementById('createMenuMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un precio válido mayor que 0.</p>';
        return;
    }

    const menu = {
        foodTruckId: parseInt(foodTruckId),
        nombre,
        descripcion: descripcion || null,
        precio: parseFloat(precio)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/menus`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(menu)
        });
        if (response.ok) {
            document.getElementById('createMenuMessage').innerHTML = '<p class="text-success">Menú creado con éxito.</p>';
            document.getElementById('createMenuForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('createMenuMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo crear el menú.'}</p>`;
        }
    } catch (error) {
        console.error('Error al crear menú:', error);
        document.getElementById('createMenuMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

async function createMenus() {
    const foodTruckId = parseInt(document.getElementById('bulkFoodTruckId').value);
    const menuItems = document.querySelectorAll('.menu-item');
    const menus = Array.from(menuItems).map(item => ({
        foodTruckId,
        nombre: item.querySelector('.menu-nombre').value,
        descripcion: item.querySelector('.menu-descripcion').value,
        precio: parseFloat(item.querySelector('.menu-precio').value)
    }));

    try {
        const response = await fetch(`${API_BASE_URL}/menus/bulk`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(menus)
        });
        if (response.ok) {
            document.getElementById('createMenusMessage').innerHTML = '<p class="text-success">Menús creados con éxito.</p>';
            document.getElementById('createMenusForm').reset();
            document.getElementById('menuItemsContainer').innerHTML = getInitialMenuItem();
        } else {
            const errorData = await response.json();
            document.getElementById('createMenusMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudieron crear los menús.'}</p>`;
        }
    } catch (error) {
        console.error('Error al crear menús:', error);
        document.getElementById('createMenusMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

async function updateMenu() {
    const menuId = parseInt(document.getElementById('menuIdUpdate').value);
    const menu = {};

    // Only include fields that have values
    const foodTruckId = document.getElementById('updateFoodTruckId').value.trim();
    const nombre = document.getElementById('updateNombre').value.trim();
    const descripcion = document.getElementById('updateDescripcion').value.trim();
    const precio = document.getElementById('updatePrecio').value.trim();

    if (foodTruckId) menu.foodTruckId = parseInt(foodTruckId);
    if (nombre) menu.nombre = nombre;
    if (descripcion) menu.descripcion = descripcion;
    if (precio) menu.precio = parseFloat(precio);

    // Validate that at least one field is provided
    if (Object.keys(menu).length === 0) {
        document.getElementById('updateMenuMessage').innerHTML = '<p class="text-danger">Por favor, proporciona al menos un campo para actualizar.</p>';
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/menus/${menuId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(menu)
        });
        if (response.ok) {
            document.getElementById('updateMenuMessage').innerHTML = '<p class="text-success">Menú modificado con éxito.</p>';
            document.getElementById('updateMenuForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('updateMenuMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo modificar el menú.'}</p>`;
        }
    } catch (error) {
        console.error('Error al modificar menú:', error);
        document.getElementById('updateMenuMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

async function deleteMenu() {
    const menuId = parseInt(document.getElementById('menuIdDelete').value);
    try {
        const response = await fetch(`${API_BASE_URL}/menus/${menuId}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            document.getElementById('deleteMenuMessage').innerHTML = '<p class="text-success">Menú eliminado con éxito.</p>';
            document.getElementById('deleteMenuForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('deleteMenuMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo eliminar el menú.'}</p>`;
        }
    } catch (error) {
        console.error('Error al eliminar menú:', error);
        document.getElementById('deleteMenuMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

async function createFoodTruck() {
    const nombre = document.getElementById('nombre').value.trim();
    const tipoCocina = document.getElementById('tipoCocina').value.trim();
    const ubicacionActual = document.getElementById('ubicacionActual').value.trim();

    // Validate required fields
    if (!nombre) {
        document.getElementById('createFoodTruckMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un nombre para el food truck.</p>';
        return;
    }
    if (!tipoCocina) {
        document.getElementById('createFoodTruckMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un tipo de cocina.</p>';
        return;
    }
    if (!ubicacionActual) {
        document.getElementById('createFoodTruckMessage').innerHTML = '<p class="text-danger">Por favor, ingresa una ubicación actual.</p>';
        return;
    }

    const foodTruck = {
        nombre,
        tipoCocina,
        ubicacionActual
    };

    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(foodTruck)
        });
        if (response.ok) {
            document.getElementById('createFoodTruckMessage').innerHTML = '<p class="text-success">Food Truck creado con éxito.</p>';
            document.getElementById('createFoodTruckForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('createFoodTruckMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo crear el food truck.'}</p>`;
        }
    } catch (error) {
        console.error('Error al crear food truck:', error);
        document.getElementById('createFoodTruckMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

async function updateFoodTruck() {
    const foodTruckId = parseInt(document.getElementById('foodTruckIdUpdate').value);
    const foodTruck = {};

    // Only include fields that have values
    const nombre = document.getElementById('updateNombre').value.trim();
    const tipoCocina = document.getElementById('updateTipoCocina').value.trim();
    const ubicacionActual = document.getElementById('updateUbicacionActual').value.trim();

    if (nombre) foodTruck.nombre = nombre;
    if (tipoCocina) foodTruck.tipoCocina = tipoCocina;
    if (ubicacionActual) foodTruck.ubicacionActual = ubicacionActual;

    // Validate that at least one field is provided
    if (Object.keys(foodTruck).length === 0) {
        document.getElementById('updateFoodTruckMessage').innerHTML = '<p class="text-danger">Por favor, proporciona al menos un campo para actualizar.</p>';
        return;
    }

    // Validate ID
    if (isNaN(foodTruckId) || foodTruckId <= 0) {
        document.getElementById('updateFoodTruckMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un ID de Food Truck válido (número positivo).</p>';
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks/${foodTruckId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(foodTruck)
        });
        if (response.ok) {
            document.getElementById('updateFoodTruckMessage').innerHTML = '<p class="text-success">Food Truck modificado con éxito.</p>';
            document.getElementById('updateFoodTruckForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('updateFoodTruckMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo modificar el food truck.'}</p>`;
        }
    } catch (error) {
        console.error('Error al modificar food truck:', error);
        document.getElementById('updateFoodTruckMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

async function deleteFoodTruck() {
    const foodTruckId = parseInt(document.getElementById('foodTruckIdDelete').value);

    // Validate ID
    if (isNaN(foodTruckId) || foodTruckId <= 0) {
        document.getElementById('deleteFoodTruckMessage').innerHTML = '<p class="text-danger">Por favor, ingresa un ID de Food Truck válido (número positivo).</p>';
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/foodtrucks/${foodTruckId}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            document.getElementById('deleteFoodTruckMessage').innerHTML = '<p class="text-success">Food Truck eliminado con éxito.</p>';
            document.getElementById('deleteFoodTruckForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('deleteFoodTruckMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo eliminar el food truck.'}</p>`;
        }
    } catch (error) {
        console.error('Error al eliminar food truck:', error);
        document.getElementById('deleteFoodTruckMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor.</p>';
    }
}

function addMenuItem() {
    const container = document.getElementById('menuItemsContainer');
    const count = container.children.length + 1;
    const newItem = `
        <div class="menu-item mb-3">
            <h5>Menú ${count}</h5>
            <div class="mb-2">
                <label class="form-label">Nombre</label>
                <input type="text" class="form-control menu-nombre" placeholder="Nombre del menú" required>
            </div>
            <div class="mb-2">
                <label class="form-label">Descripción</label>
                <textarea class="form-control menu-descripcion" placeholder="Descripción"></textarea>
            </div>
            <div class="mb-2">
                <label class="form-label">Precio</label>
                <input type="number" step="0.01" class="form-control menu-precio" placeholder="Precio" required>
            </div>
        </div>
    `;
    container.innerHTML += newItem;
}

function getInitialMenuItem() {
    return `
        <div class="menu-item mb-3">
            <h5>Menú 1</h5>
            <div class="mb-2">
                <label class="form-label">Nombre</label>
                <input type="text" class="form-control menu-nombre" placeholder="Nombre del menú" required>
            </div>
            <div class="mb-2">
                <label class="form-label">Descripción</label>
                <textarea class="form-control menu-descripcion" placeholder="Descripción"></textarea>
            </div>
            <div class="mb-2">
                <label class="form-label">Precio</label>
                <input type="number" step="0.01" class="form-control menu-precio" placeholder="Precio" required>
            </div>
        </div>
    `;
}

async function register() {
    const usuario = {
        nombre: document.getElementById('registerNombre').value,
        email: document.getElementById('registerEmail').value,
        password: document.getElementById('registerPassword').value,
        ubicacion: document.getElementById('registerUbicacion').value || null
    };
    try {
        const response = await fetch(`${API_BASE_URL}/usuarios`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });
        if (response.ok) {
            document.getElementById('registerMessage').innerHTML = '<p class="text-success">Registro exitoso. ¡Bienvenido a Truck Bites!</p>';
            document.getElementById('registerForm').reset();
        } else {
            const errorData = await response.json();
            document.getElementById('registerMessage').innerHTML = `<p class="text-danger">Error: ${errorData.message || 'No se pudo registrar. Intenta de nuevo.'}</p>`;
        }
    } catch (error) {
        console.error('Error al registrarse:', error);
        document.getElementById('registerMessage').innerHTML = '<p class="text-danger">Error al conectar con el servidor. Intenta de nuevo.</p>';
    }
}