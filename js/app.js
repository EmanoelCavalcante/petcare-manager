// Menu navigation
const menuItems = document.querySelectorAll(".menu-item");
const pages = document.querySelectorAll(".page");

// Client management
const formClient = document.querySelector("#formCliente");
const nomeClient = document.querySelector("#nomeCliente");
const telefoneClient = document.querySelector("#telefoneCliente");
const enderecoClient = document.querySelector("#enderecoCliente");

// Client list and total
const listClients = document.querySelector("#listaClientes");
const totalClients = document.querySelector("#totalClientes");

// Dashboard elements
const dashboardTotalClients = document.querySelector("#dashboardTotalClientes");
const dashboardTotalPets = document.querySelector("#dashboardTotalPets");
const dashboardTotalAgendamentos = document.querySelector("#dashboardTotalAgendamentos");
const dashboardTotalServios = document.querySelector("#dashboardTotalServicos");

const dashboardTotalClientesBadge = document.querySelector("#dashboardClientesBadge");
const dashboardTotalClientesList = document.querySelector("#dashboardClientesList");

// Buttons for dashboard
const btnParaClientes = document.querySelector("#btnParaClientes");
const btnParaAgendamentos = document.querySelector("#btnParaAgendamentos");

// Initialize clients array
let clients = [];

// Function to show a specific page and highlight the corresponding menu item
function showPage(pageId) {
    menuItems.forEach(function (item) {
        item.classList.remove("active");
    });

    pages.forEach(function (page) {
        page.classList.remove("active");
    });

    const activeMenuItem = document.querySelector(`.menu-item[data-page="${pageId}"]`);
    const activePage = document.querySelector(`#${pageId}`);

    activeMenuItem.classList.add("active");
    activePage.classList.add("active");
}

// LocalStorage functions to save and load clients
function saveClients() {
    localStorage.setItem("clients", JSON.stringify(clients));
}

function loadClients() {
    const storedClients = localStorage.getItem("clients");
    if (storedClients) {
        clients = JSON.parse(storedClients);
    }
}

// Function to render the list of clients
function renderClients() {
    listClients.innerHTML = "";

    if (clients.length === 0) {
        istItem.innerHTML = `
        <div class="client-info">
            <h4>${client.nome}</h4>
            <p>Telefone: ${client.telefone}</p>
            <p>Endereço: ${client.endereco}</p>
        </div>
        `;
        totalClients.textContent = "0 clientes";
        return;
    }

    clients.forEach(function (client) {
        const listItem = document.createElement("div");
        listItem.classList.add("client-card");

        listClients.appendChild(listItem);
    });
}

// Function to update the dashboard with the total number of clients
function updateDashboard() {
    dashboardTotalClientes.textContent = clients.length;
    dashboardTotalAgendamentos.textContent = 0;
    dashboardTotalPets.textContent = 0;
    dashboardTotalServios.textContent = 0;

    dashboardTotalClientesBadge.textContent = `${clients.length} clientes`;

    dashboardTotalClientesList.innerHTML = "";

    if (clients.length === 0) {
        dashboardTotalClientesList.innerHTML = `
        <div class="empty-state">
            <div>👤</div>
            <h4>Nenhum cliente cadastrado.</h4>
            <p>Os últimos clientes cadastrados irão aparecer aqui.</p>
        </div>
        '`;
        return;
    }

    const recentClients = clients.slice(-3).reverse();

    recentClients.forEach(function (client) {
        const clientCard = document.createElement("div");
        clientCard.classList.add("client-card");

        clientCard.innerHTML = `
        <div class="client-info>
                <h4 class="client-name">${client.nome}</h4>
                <p class="client-phone">Telefone: ${client.telefone}</p>
        </div>"`;

        dashboardTotalClientesList.appendChild(clientCard);
    });
}

// Function to create a handle submit for "clientes" and "agendamentos"
function handleClientSubmit(event) {
    event.preventDefault();

    const nome = nomeClient.value.trim();
    const telefone = telefoneClient.value.trim();
    const endereco = enderecoClient.value.trim();

    if (nome === "" || telefone === "" || endereco === "") {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    for (let i = 0; i < clients.length; i++) {
        if (clients[i] && clients[i].nome === nome && clients[i].telefone === telefone) {
            alert("Este cliente já está cadastrado.");
            return;
        }
    }

    const client = {
        id: Date.now(),
        nome: nome,
        telefone: telefone,
        endereco: endereco
    }

    clients.push(client);

    saveClients();
    renderClients();
    updateDashboard();

    formClient.reset();
}

// eventListner for to show pages with in menu
menuItems.forEach(function (menuItem) {
    menuItem.addEventListener("click", function () {
        event.preventDefault();

        const pageId = menuItem.dataset.page;

        showPage(pageId);
    });
});

formClient.addEventListener("submit", handleClientSubmit);

btnParaClientes.addEventListener("click", function() {
    showPage("clientes");
});

btnParaAgendamentos.addEventListener("click", function() {
    showPage("agendamentos");
});

//Initialize a dashboard 
loadClients();
renderClients();
updateDashboard();   