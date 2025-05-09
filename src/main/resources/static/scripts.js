/**
 * Scripts para interactuar con la API de Truck Bites.
 */
const API_BASE_URL = 'http://localhost:8080/api';

// Cargar contenido al iniciar la página
document.addEventListener('DOMContentLoaded', () => {
    // Cargar food trucks cercanos en index.html
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const ciudad = document.getElementById('ciudad').value;
            const calle = document.getElementById('calle').value;
            loadFoodTrucksCercanos(ciudad, calle);
        });
        loadFoodTrucksCercanos('Nueva York', '');
    }

    // Cargar todos los food trucks en foodtrucks.html
    if (window.location.pathname.includes('foodtrucks.html')) {
        loadAllFoodTrucks();
    }

    // Cargar menús en menu.html
    if (window.location.pathname.includes('menu.html')) {
        const urlParams = new URLSearchParams(window.location.search);
        const foodTruckId = urlParams.get('foodTruckId');
        const foodTruckName = urlParams.get('foodTruckName') || 'Menú';
        document.getElementById('foodTruckName').textContent = `Menú de ${foodTruckName}`;
        if (foodTruckId) {
            loadMenus(foodTruckId);
            // Actualizar enlace de pedido
            const pedidoLink = document.querySelector('a[href="pedido.html"]');
            if (pedidoLink) {
                pedidoLink.href = `pedido.html?foodTruckId=${foodTruckId}`;
            }
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

    // Manejar registro en register.html
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await register();
        });
    }
});

// Cargar todos los food trucks
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
                        <img src="https://via.placeholder.com/300x200" class="card-img-top" alt="${truck.nombre}">
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

// Cargar food trucks cercanos
async function loadFoodTrucksCercanos(ciudad, calle) {
    try {
        const url = new URL(`${API_BASE_URL}/foodtrucks/cerca`);
        url.searchParams.append('ciudad', ciudad);
        if (calle) url.searchParams.append('calle', calle);
        const response = await fetch(url);
        const foodTrucks = await response.json();
        const foodTrucksList = document.getElementById('foodTrucksList');
        foodTrucksList.innerHTML = '';
        foodTrucks.forEach(truck => {
            const card = `
                <div class="col">
                    <div class="card h-100">
                        <img src="https://via.placeholder.com/300x200" class="card-img-top" alt="${truck.nombre}">
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
    }
}

// Cargar menús de un food truck (para menu.html)
async function loadMenus(foodTruckId) {
    try {
        const response = await fetch(`${API_BASE_URL}/menus/foodtruck/${foodTruckId}`);
        const menus = await response.json();
        const menuList = document.getElementById('menuList');
        menuList.innerHTML = '';
        menus.forEach(menu => {
            const card = `
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <img src="https://via.placeholder.com/300x200" class="card-img-top" alt="${menu.nombre}">
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
    }
}

// Cargar menús para selección en pedido.html
async function loadMenusForPedido(foodTruckId) {
    try {
        const response = await fetch(`${API_BASE_URL}/menus/foodtruck/${foodTruckId}`);
        const menus = await response.json();
        const menuItems = document.getElementById('menuItems');
        menuItems.innerHTML = '';
        menus.forEach(menu => {
            const item = `
                <div class="list-group-item">
                    <input type="checkbox" class="form-check-input me-2" id="menu-${menu.id}" value="${menu.nombre}" data-precio="${menu.precio}">
                    <label class="form-check-label" for="menu-${menu.id}">${menu.nombre} ($${menu.precio.toFixed(2)})</label>
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

// Actualizar ítems seleccionados y monto total
function updatePedido() {
    const checkboxes = document.querySelectorAll('#menuItems input:checked');
    const items = Array.from(checkboxes).map(cb => cb.value).join(', ');
    const montoTotal = Array.from(checkboxes).reduce((sum, cb) => sum + parseFloat(cb.dataset.precio), 0);
    document.getElementById('items').value = items;
    document.getElementById('montoTotal').value = montoTotal.toFixed(2);
}

// Enviar un pedido
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

// Cargar notificaciones de un usuario
async function loadNotificaciones() {
    const usuarioId = document.getElementById('usuarioIdNotificaciones').value;
    try {
        const response = await fetch(`${API_BASE_URL}/notificaciones/usuario/${usuarioId}`);
        const notificaciones = await response.json();
        const notificacionesList = document.getElementById('notificacionesList');
        notificacionesList.innerHTML = '';
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
    }
}

// Registrar un nuevo usuario
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