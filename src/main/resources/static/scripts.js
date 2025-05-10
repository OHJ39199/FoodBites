/**
 * Scripts para interactuar con la API de Truck Bites.
 */
const API_BASE_URL = 'http://localhost:8080/api';

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

    // No se agrega lógica adicional para notificaciones.html aquí, ya que los botones llaman a loadNotificaciones y loadPedidos directamente
});

// Cargar food trucks para el carrusel en index.html
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
                    <img src="https://via.placeholder.com/1200x400" class="d-block w-100" alt="${truck.nombre}">
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

// Cargar menús de un food truck específico
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
                        <img src="https://via.placeholder.com/300x200" class="card-img-top" alt="${menu.nombre}">
                        <div class="card-body">
                            <h5 class="card-title">${menu.nombre}</h5>
                            <p class="card-text">${menu.descripcion}</p>
                            <p class="text">Precio: $${menu.precio.toFixed(2)}</p>
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

// Cargar todos los menús de todos los food trucks
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
                        <img src="https://via.placeholder.com/300x200" class="card-img-top" alt="${menu.nombre}">
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

// Cargar pedidos de un usuario
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
        pedidosList.innerHTML = `
            <table class="table table-striped table-bordered table-hover">
                <thead class="table-dark">
                    <tr>
                        <th scope="col" class="pedido-id-col">ID Pedido</th>
                        <th scope="col" class="foodtruck-col">Food Truck</th>
                        <th scope="col" class="items-col">Ítems</th>
                        <th scope="col" class="monto-col">Monto Total</th>
                        <th scope="col" class="estado-col">Estado</th>
                        <th scope="col" class="fecha-col">Fecha Creación</th>
                    </tr>
                </thead>
                <tbody>
        `;
        pedidos.forEach(pedido => {
            const itemsTruncated = pedido.items.length > 50 ? pedido.items.substring(0, 47) + '...' : pedido.items;
            pedidosList.innerHTML += `
                <tr>
                    <td class="pedido-id-col">${pedido.id}</td>
                    <td class="foodtruck-col">${pedido.foodTruckNombre}</td>
                    <td class="items-col" title="${pedido.items}">${itemsTruncated}</td>
                    <td class="monto-col">$${pedido.montoTotal.toFixed(2)}</td>
                    <td class="estado-col">${pedido.estado}</td>
                    <td class="fecha-col">${new Date(pedido.fechaCreacion).toLocaleString()}</td>
                </tr>
            `;
        });
        pedidosList.innerHTML += '</tbody></table>';
    } catch (error) {
        console.error('Error al cargar pedidos:', error);
        document.getElementById('pedidosList').innerHTML = '<p class="text-danger text-center">Error al cargar los pedidos.</p>';
    }
}