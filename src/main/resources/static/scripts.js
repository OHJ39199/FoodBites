/**
 * Scripts para interactuar con la API de Truck Bites.
 */
const API_BASE_URL = 'http://localhost:8080/api';

// Mostrar modal de login al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    const loginModal = new bootstrap.Modal(document.getElementById('loginModal'), { backdrop: 'static' });
    if (!localStorage.getItem('token')) {
        loginModal.show();
    }

    // Manejar formulario de login
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await login();
            loginModal.hide();
        });
    }

    // Cargar food trucks cercanos en index.html
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const ciudad = document.getElementById('ciudad').value;
            const calle = document.getElementById('calle').value;
            loadFoodTrucksCercanos(ciudad, calle);
        });
        if (localStorage.getItem('token')) {
            loadFoodTrucksCercanos('Nueva York', '');
        }
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
            pedidoLink.href = `pedido.html?foodTruckId=${foodTruckId}`;
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
});

// Iniciar sesión
async function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            localStorage.setItem('usuarioId', data.usuarioId);
            alert('Inicio de sesión exitoso');
            // Recargar food trucks si estamos en index.html
            if (window.location.pathname.includes('index.html')) {
                loadFoodTrucksCercanos('Nueva York', '');
            }
        } else {
            alert('Error al iniciar sesión');
        }
    } catch (error) {
        console.error('Error al iniciar sesión:', error);
        alert('Error al iniciar sesión');
    }
}

// Cargar food trucks cercanos
async function loadFoodTrucksCercanos(ciudad, calle) {
    try {
        const url = new URL(`${API_BASE_URL}/foodtrucks/cerca`);
        url.searchParams.append('ciudad', ciudad);
        if (calle) url.searchParams.append('calle', calle);
        const response = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const foodTrucks = await response.json();
        const foodTrucksList = document.getElementById('foodTrucksList');
        foodTrucksList.innerHTML = '';
        foodTrucks.forEach(truck => {
            const card = `
                <div class="col-md-4 mb-4">
                    <div class="card">
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
        const response = await fetch(`${API_BASE_URL}/menus/foodtruck/${foodTruckId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
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
        const response = await fetch(`${API_BASE_URL}/menus/foodtruck/${foodTruckId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
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
        usuarioId: parseInt(localStorage.getItem('usuarioId') || document.getElementById('usuarioId').value),
        foodTruckId: document.getElementById('foodTruckId').value,
        items: document.getElementById('items').value,
        montoTotal: parseFloat(document.getElementById('montoTotal').value),
        estado: 'PENDIENTE'
    };
    try {
        const response = await fetch(`${API_BASE_URL}/pedidos`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
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
    const usuarioId = localStorage.getItem('usuarioId') || document.getElementById('usuarioIdNotificaciones').value;
    try {
        const response = await fetch(`${API_BASE_URL}/notificaciones/usuario/${usuarioId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
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
