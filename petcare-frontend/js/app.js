// =========================
// ELEMENTOS DA NAVEGAÇÃO
// =========================

const menuItems = document.querySelectorAll(
    ".menu-item"
);

const pages = document.querySelectorAll(".page");

// =========================
// BOTÕES DO DASHBOARD
// =========================

const btnParaClientes = document.querySelector(
    "#btnParaClientes"
);

const btnParaAgendamentos = document.querySelector(
    "#btnParaAgendamentos"
);

// =========================
// TOTAIS AINDA NÃO IMPLEMENTADOS
// =========================

const dashboardTotalPets = document.querySelector(
    "#dashboardTotalPets"
);

const dashboardTotalAgendamentos =
    document.querySelector(
        "#dashboardTotalAgendamentos"
    );

const dashboardTotalServicos =
    document.querySelector(
        "#dashboardTotalServicos"
    );

// =========================
// NAVEGAÇÃO
// =========================

function showPage(pageId) {
    const activePage = document.querySelector(
        `#${pageId}`
    );

    const activeMenuItem = document.querySelector(
        `.menu-item[data-page="${pageId}"]`
    );

    if (!activePage) {
        console.error(
            `A página "${pageId}" não foi encontrada.`
        );

        return;
    }

    pages.forEach(function (page) {
        page.classList.remove("active");
    });

    menuItems.forEach(function (menuItem) {
        menuItem.classList.remove("active");
    });

    activePage.classList.add("active");

    if (activeMenuItem) {
        activeMenuItem.classList.add("active");
    }
}

function configureNavigation() {
    menuItems.forEach(function (menuItem) {
        menuItem.addEventListener(
            "click",
            function (event) {
                event.preventDefault();

                const pageId =
                    menuItem.dataset.page;

                if (!pageId) {
                    return;
                }

                showPage(pageId);
            }
        );
    });

    btnParaClientes?.addEventListener(
        "click",
        function () {
            showPage("clientes");
        }
    );

    btnParaAgendamentos?.addEventListener(
        "click",
        function () {
            showPage("agendamentos");
        }
    );
}

// =========================
// DASHBOARD
// =========================

function initializeDashboardTotals() {
    if (dashboardTotalPets) {
        dashboardTotalPets.textContent = "0";
    }

    if (dashboardTotalAgendamentos) {
        dashboardTotalAgendamentos.textContent =
            "0";
    }

    if (dashboardTotalServicos) {
        dashboardTotalServicos.textContent = "0";
    }
}

// =========================
// INICIALIZAÇÃO DA APLICAÇÃO
// =========================

function initializeApplication() {
    configureNavigation();
    initializeDashboardTotals();

    if (
        typeof initializeCliente === "function"
    ) {
        initializeCliente();
    } else {
        console.error(
            "A função initializeCliente não foi encontrada."
        );
    }
}

document.addEventListener(
    "DOMContentLoaded",
    initializeApplication
);