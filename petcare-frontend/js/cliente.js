const CLIENTS_STORAGE_KEY = "petcare_clientes";

let clients = [];

// ==============================
// ELEMENTOS DO FORMULÁRIO
// ==============================

const formCliente = document.querySelector("#formCliente");

const nomeClienteInput = document.querySelector(
    "#nomeCliente"
);

const telefoneClienteInput = document.querySelector(
    "#telefoneCliente"
);

const enderecoClienteInput = document.querySelector(
    "#enderecoCliente"
);

// ==============================
// ELEMENTOS DA LISTA
// ==============================

const listaClientes = document.querySelector(
    "#listaClientes"
);

const totalClientes = document.querySelector(
    "#totalClientes"
);

// ==============================
// ELEMENTOS DO DASHBOARD
// ==============================

const dashboardTotalClientes = document.querySelector(
    "#dashboardTotalClientes"
);

const dashboardClientesBadge = document.querySelector(
    "#dashboardClientesBadge"
);

const dashboardClientesList = document.querySelector(
    "#dashboardClientesList"
);

// ==============================
// LOCAL STORAGE
// ==============================

function loadClientes() {
    const dadosSalvos = localStorage.getItem(
        CLIENTS_STORAGE_KEY
    );

    if (!dadosSalvos) {
        return [];
    }

    try {
        const dadosConvertidos = JSON.parse(
            dadosSalvos
        );

        if (!Array.isArray(dadosConvertidos)) {
            return [];
        }

        return dadosConvertidos;
    } catch (erro) {
        console.error(
            "Não foi possível carregar os clientes.",
            erro
        );

        return [];
    }
}

function saveClientes() {
    const clientesEmJSON = JSON.stringify(
        clients
    );

    localStorage.setItem(
        CLIENTS_STORAGE_KEY,
        clientesEmJSON
    );
}

// ==============================
// LIMPEZA E VALIDAÇÃO
// ==============================

function toCleanTelefone(telefone) {
    return telefone.replace(/\D/g, "");
}

function validCliente(
    nome,
    telefone,
    endereco
) {
    if (nome === "") {
        throw new Error(
            "O nome é obrigatório."
        );
    }

    if (nome.length < 3) {
        throw new Error(
            "O nome deve possuir pelo menos 3 caracteres."
        );
    }

    if (telefone === "") {
        throw new Error(
            "O telefone é obrigatório."
        );
    }

    if (
        telefone.length !== 10 &&
        telefone.length !== 11
    ) {
        throw new Error(
            "O telefone deve possuir 10 ou 11 números."
        );
    }

    if (endereco === "") {
        throw new Error(
            "O endereço é obrigatório."
        );
    }
}

// ==============================
// VERIFICAÇÃO DE DUPLICIDADE
// ==============================

function alreadyExistsCliente(
    nome,
    telefone
) {
    return clients.some(
        function (cliente) {
            const mesmoNome =
                cliente.nome.toLowerCase() ===
                nome.toLowerCase();

            const mesmoTelefone =
                cliente.telefone === telefone;

            const clienteAtivo =
                cliente.ativo !== false;

            return (
                mesmoNome &&
                mesmoTelefone &&
                clienteAtivo
            );
        }
    );
}

// ==============================
// BUSCA DE CLIENTE
// ==============================

function findClienteById(id) {
    return clients.find(
        function (cliente) {
            return cliente.id === id;
        }
    );
}

// ==============================
// CRIAÇÃO DE CLIENTE
// ==============================

function createCliente(
    nome,
    telefone,
    endereco
) {
    const nomeLimpo = nome.trim();

    const telefoneLimpo =
        toCleanTelefone(telefone);

    const enderecoLimpo =
        endereco.trim();

    validCliente(
        nomeLimpo,
        telefoneLimpo,
        enderecoLimpo
    );

    if (
        alreadyExistsCliente(
            nomeLimpo,
            telefoneLimpo
        )
    ) {
        throw new Error(
            "Este cliente já está cadastrado."
        );
    }

    const novoCliente = {
        id: Date.now(),
        nome: nomeLimpo,
        telefone: telefoneLimpo,
        endereco: enderecoLimpo,
        criadoEm: new Date().toISOString(),
        ativo: true
    };

    clients.push(novoCliente);

    saveClientes();

    return novoCliente;
}

// ==============================
// DESATIVAÇÃO LÓGICA
// ==============================

function deactivateCliente(id) {
    const cliente = findClienteById(id);

    if (!cliente) {
        throw new Error(
            "Cliente não encontrado."
        );
    }

    if (cliente.ativo === false) {
        throw new Error(
            "Este cliente já está desativado."
        );
    }

    cliente.ativo = false;

    saveClientes();

    return cliente;
}

// ==============================
// CRIAÇÃO DO CARD
// ==============================

function createCardCliente(
    cliente,
    mostrarEndereco = true,
    mostrarAcoes = true
) {
    const card = document.createElement(
        "div"
    );

    card.classList.add("client-card");

    const clienteInfo =
        document.createElement("div");

    clienteInfo.classList.add(
        "client-info"
    );

    const nome = document.createElement(
        "h4"
    );

    nome.classList.add("client-name");
    nome.textContent = cliente.nome;

    const telefone =
        document.createElement("p");

    telefone.classList.add(
        "client-phone"
    );

    telefone.textContent =
        `Telefone: ${cliente.telefone}`;

    clienteInfo.appendChild(nome);
    clienteInfo.appendChild(telefone);

    if (mostrarEndereco) {
        const endereco =
            document.createElement("p");

        endereco.classList.add(
            "client-address"
        );

        endereco.textContent =
            `Endereço: ${cliente.endereco}`;

        clienteInfo.appendChild(
            endereco
        );
    }

    card.appendChild(clienteInfo);

    if (mostrarAcoes) {
        const clienteActions =
            document.createElement("div");

        clienteActions.classList.add(
            "client-actions"
        );

        const btnDesativar =
            document.createElement("button");

        btnDesativar.type = "button";

        btnDesativar.classList.add(
            "btn-danger"
        );

        btnDesativar.textContent =
            "Desativar";

        btnDesativar.addEventListener(
            "click",
            function () {
                const confirmou =
                    window.confirm(
                        `Deseja desativar o cliente ${cliente.nome}?`
                    );

                if (!confirmou) {
                    return;
                }

                try {
                    deactivateCliente(
                        cliente.id
                    );

                    renderingClientes();

                    toUpdateDashboardClientes();
                } catch (erro) {
                    alert(erro.message);
                }
            }
        );

        clienteActions.appendChild(
            btnDesativar
        );

        card.appendChild(
            clienteActions
        );
    }

    return card;
}

// ==============================
// CLIENTES ATIVOS
// ==============================

function getClientesAtivos() {
    return clients.filter(
        function (cliente) {
            return cliente.ativo !== false;
        }
    );
}

// ==============================
// RENDERIZAÇÃO DA LISTA
// ==============================

function renderingClientes() {
    if (
        !listaClientes ||
        !totalClientes
    ) {
        return;
    }

    listaClientes.innerHTML = "";

    const clientesAtivos =
        getClientesAtivos();

    const quantidade =
        clientesAtivos.length;

    totalClientes.textContent =
        quantidade === 1
            ? "1 cliente"
            : `${quantidade} clientes`;

    if (quantidade === 0) {
        listaClientes.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">
                    👤
                </div>

                <h4>
                    Nenhum cliente cadastrado.
                </h4>

                <p>
                    Cadastre o primeiro cliente
                    utilizando o formulário acima.
                </p>
            </div>
        `;

        return;
    }

    clientesAtivos.forEach(
        function (cliente) {
            const card =
                createCardCliente(
                    cliente,
                    true,
                    true
                );

            listaClientes.appendChild(
                card
            );
        }
    );
}

// ==============================
// ATUALIZAÇÃO DO DASHBOARD
// ==============================

function toUpdateDashboardClientes() {
    const clientesAtivos =
        getClientesAtivos();

    const quantidade =
        clientesAtivos.length;

    if (dashboardTotalClientes) {
        dashboardTotalClientes.textContent =
            quantidade;
    }

    if (dashboardClientesBadge) {
        dashboardClientesBadge.textContent =
            quantidade === 1
                ? "1 cliente"
                : `${quantidade} clientes`;
    }

    if (!dashboardClientesList) {
        return;
    }

    dashboardClientesList.innerHTML = "";

    if (quantidade === 0) {
        dashboardClientesList.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">
                    👤
                </div>

                <h4>
                    Nenhum cliente cadastrado.
                </h4>

                <p>
                    Os últimos clientes cadastrados
                    irão aparecer aqui.
                </p>
            </div>
        `;

        return;
    }

    const clientesRecentes =
        clientesAtivos
            .slice(-3)
            .reverse();

    clientesRecentes.forEach(
        function (cliente) {
            const card =
                createCardCliente(
                    cliente,
                    false,
                    false
                );

            dashboardClientesList.appendChild(
                card
            );
        }
    );
}

// ==============================
// EVENTO DO FORMULÁRIO
// ==============================

function handleClienteSubmit(event) {
    event.preventDefault();

    if (
        !formCliente ||
        !nomeClienteInput ||
        !telefoneClienteInput ||
        !enderecoClienteInput
    ) {
        console.error(
            "O formulário de clientes não foi encontrado."
        );

        return;
    }

    try {
        const clienteCriado =
            createCliente(
                nomeClienteInput.value,
                telefoneClienteInput.value,
                enderecoClienteInput.value
            );

        console.log(
            "Cliente criado:",
            clienteCriado
        );

        renderingClientes();

        toUpdateDashboardClientes();

        formCliente.reset();

        nomeClienteInput.focus();
    } catch (erro) {
        alert(erro.message);
    }
}

// ==============================
// INICIALIZAÇÃO
// ==============================

function initializeCliente() {
    clients = loadClientes();

    formCliente?.addEventListener(
        "submit",
        handleClienteSubmit
    );

    renderingClientes();

    toUpdateDashboardClientes();
}